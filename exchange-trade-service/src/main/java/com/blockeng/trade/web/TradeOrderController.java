package com.blockeng.trade.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.dto.MessagePayload;
import com.blockeng.framework.enums.OrderStatus;
import com.blockeng.framework.enums.OrderType;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.trade.dto.CreateTradeOrderDTO;
import com.blockeng.trade.dto.EntrustOrderMatchDTO;
import com.blockeng.trade.dto.EntrustOrderMatchDTOMapper;
import com.blockeng.trade.dto.TradeDealDTO;
import com.blockeng.trade.entity.EntrustOrder;
import com.blockeng.trade.entity.Market;
import com.blockeng.trade.service.EntrustOrderService;
import com.blockeng.trade.service.MarketService;
import com.blockeng.rabbit.support.RabbitTemplateSupport;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/18 下午10:09
 * @Modified by: Chen Long
 * 1.mysql存入成功，mq发送失败，导致该单不撮合，match重启生效，如果不重启，无法撤单，几率小
 * 2.match宕机时，mq继续被写入，当match重启时，会从mysql读取订单，后又从mq接收到订单，同一订单读取两次
 * 3.结算延迟，已经在mq中排队，这时match宕机，match重新读取mysql，这时deal没有被扣掉，会重新用来撮合，相当于mq未结算的金额又增加了一倍
 * 4.与3结果相同，先关掉boss，撮合单进入mq，未被消费，然后关掉match，然后启动match，会读取deal没被扣掉的委托信息，然后再启动boss，撮合单被消费，但是match里订单仍是没有deal的单，导致刚才mq中未结算金额增加了一倍
 * 5.结算有并发问题,如果tx队列，多个相同id的单瞬时一起被boss消费，一般在boss被重启，撮合继续进行的时候
 */
@RestController
@RequestMapping("/trade/order")
@Slf4j
@Api(value = "币币交易订单", description = "币币交易订单 REST API")
public class TradeOrderController {

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private RabbitTemplateSupport templateSupport;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 币币交易委托下单
     *
     * @param order 委托下单请求参数
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/entrusts")
    @ApiOperation(value = "ORDER-001 币币交易委托下单", notes = "币币交易委托下单", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "order", value = "币币交易委托下单请求参数", required = true, dataType = "CreateTradeOrderDTO", paramType = "body")
    public Object createEntrustOrder(@RequestBody @Valid CreateTradeOrderDTO order,
                                     @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                                     BindingResult result) {
        Market market = marketService.queryBySymbol(order.getSymbol());
        if (market == null) {
            log.error("交易市场错误");
            throw new GlobalDefaultException(50002);
        }
        EntrustOrder entrustOrder = entrustOrderService.createEntrustOrder(market, order, userDetails.getId());
        mongoTemplate.insert(entrustOrder);
        EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
        if (order.getType() == OrderType.BUY.getCode()) {
            entrustOrderDTO.setCoinId(market.getBuyCoinId());
        } else {
            entrustOrderDTO.setCoinId(market.getSellCoinId());
        }
        entrustOrderDTO.setCreated(new DateTime().toDate());
        String json = new Gson().toJson(entrustOrderDTO);
        templateSupport.convertAndSend("order.in", json);
        // 通知用户刷新未成交委托订单列表
        JSONObject body = new JSONObject();
        body.put("symbol", order.getSymbol());
        MessagePayload messagePayload = new MessagePayload();
        messagePayload.setUserId(String.valueOf(userDetails.getId()));
        messagePayload.setChannel(Constant.CH_ORDER_PENDING);
        messagePayload.setBody(body.toJSONString());
        kafkaTemplate.send(Constant.CHANNEL_SENDTO_USER, GsonUtil.toJson(messagePayload));
        return Response.ok(entrustOrder.getId());
    }

    /**
     * 撤销委托
     *
     * @param orderId 订单号
     * @return
     */
    @PreAuthorize("isAuthenticated()")
   // @DeleteMapping("/entrusts/{orderId}")
    @PostMapping("/entrusts/{orderId}")
    @ApiOperation(value = "ORDER-002 币币交易撤销委托", notes = "币币交易撤销委托", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "orderId", value = "币币交易委托订单号", required = true, dataType = "long", paramType = "path")
    public Object cancelEntrustOrder(@PathVariable("orderId") long orderId,
                                     @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (orderId <= 0L) {
            log.error("撤单失败，订单号错误，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
        EntrustOrder entrustOrder = entrustOrderService.selectById(orderId);
        if (entrustOrder == null) {
            log.error("撤单失败，委托订单不存在，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
        if (!entrustOrder.getUserId().equals(userDetails.getId())) {
            log.error("当前登录用户与委托订单所属用户不一致，订单所属用户：{}，当前登录用户：{}", orderId, userDetails.getId());
            throw new GlobalDefaultException(50009);
        }
        if (entrustOrder.getStatus() == OrderStatus.DEAL.getCode()) {
            return Response.err(50010, "订单已成交，无法撤销");
        } else if (entrustOrder.getStatus() == OrderStatus.CANCEL.getCode()) {

            return Response.err(50010, "订单已撤销，请勿重复操作");
        } else if (entrustOrder.getStatus() == OrderStatus.MATCH_ABNORMAL.getCode()) {

            return Response.err(50010, "订单异常，无法撤销");
        }
        entrustOrderService.startCancel(orderId);
        EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
        entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
        String json = new Gson().toJson(entrustOrderDTO);
        templateSupport.convertAndSend("order.in", json);
        return Response.ok();
    }

    /**
     * 后台撤单
     * @param orderId
     * @return
     */
    @PostMapping("/cancelOrder")
    public Object cancelEntrustOrder(String orderId) {
        if (Long.valueOf(orderId) <= 0L) {
            log.error("撤单失败，订单号错误，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
         EntrustOrder entrustOrder = entrustOrderService.selectById(Long.valueOf(orderId));
        if (entrustOrder == null) {
            log.error("撤单失败，委托订单不存在，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
        if (entrustOrder.getStatus() == OrderStatus.DEAL.getCode()) {

            return Response.err(50010, "订单已成交，无法撤销");
        } else if (entrustOrder.getStatus() == OrderStatus.CANCEL.getCode()) {

            return Response.err(50010, "订单已撤销，请勿重复操作");
        } else if (entrustOrder.getStatus() == OrderStatus.MATCH_ABNORMAL.getCode()) {

            return Response.err(50010, "订单异常，无法撤销");
        }
        EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);


        entrustOrderService.startCancel(Long.valueOf(orderId));
        // 推送撮合队列
        entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
        String json = new Gson().toJson(entrustOrderDTO);
        templateSupport.convertAndSend("order.in", json);
        return Response.ok();
    }

    /**
     * 后台批量撤单
     * @param orderIds
     * @return
     */
    @PostMapping("/batchCancelOrder")
    public Object batchCancelEntrustOrder(@RequestBody Long[] orderIds) {
        for (Long orderId : orderIds) {
            EntrustOrder entrustOrder = entrustOrderService.selectById(orderId);
            if (entrustOrder==null){
                log.error("orderId:{} 不存在",orderId);
                continue;
            }
            if(entrustOrder.getStatus()!=OrderStatus.PENDING.getCode()){
                continue;
            }
            entrustOrderService.startCancel(orderId);
            EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
            entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
            String json = new Gson().toJson(entrustOrderDTO);
            templateSupport.convertAndSend("order.in", json);
        }
        return Response.ok();
    }

    /**
     * 根据交易对撤单
     * @param symbol
     * @return
     */
    @PostMapping("/cancelOrderByMarket")
    public Object batchCancelEntrustOrder(String symbol){
        QueryWrapper<EntrustOrder> qw = new QueryWrapper<>();
        qw.eq("symbol",symbol).eq("status",OrderStatus.PENDING.getCode());
        List<EntrustOrder> entrustOrders = entrustOrderService.selectList(qw);
        entrustOrders.forEach(entrustOrder -> {
            EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
            entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
            String json = new Gson().toJson(entrustOrderDTO);
            templateSupport.convertAndSend("order.in", json);
        });
        return Response.ok();
    }

//    /**
//     * 超时自动撤单
//     * @param entrustOrder
//     */
//    @RabbitListener(queues = {DelayRabbitConfig.ORDER_QUEUE_NAME})
//    public void orderDelayQueue(EntrustOrder entrustOrder) {
//
//        for (Long orderId : orderIds) {
//            EntrustOrder entrustOrder = entrustOrderService.selectById(orderId);
//            EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
//            entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
//            String json = new Gson().toJson(entrustOrderDTO);
//            rabbitTemplate.convertAndSend("order.in", json);
//        }
//        return "Success";
//
//    }
    /**
     * 接口apiKey
     */
    @Value("${api.key}")
    private String apiKey;

    /**
     * 机器人刷单（自成交）
     *
     * @param order 成交订单
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/deal")
    @ApiOperation(value = "机器人刷单（自成交）", notes = "机器人刷单（自成交）", httpMethod = "POST",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "order", value = "币币交易机器人下单请求参数", required = true, dataType = "TradeDealDTO", paramType = "body")
    public Object dealOrder(@RequestBody @Valid TradeDealDTO order,
                            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails,
                            BindingResult result) {
        if (!apiKey.equals(order.getApi_key())) {
            return Response.err(-2, "apiKey错误");
        }
        Market market = marketService.queryBySymbol(order.getSymbol());
        if (market == null) {
            log.error("交易市场错误");
            throw new GlobalDefaultException(50002);
        }
        entrustOrderService.createOrder(market, order, userDetails.getId());
        return Response.ok();
    }
}
