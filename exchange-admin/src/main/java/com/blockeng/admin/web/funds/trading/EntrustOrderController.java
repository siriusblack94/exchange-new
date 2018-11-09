package com.blockeng.admin.web.funds.trading;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.EntrustOrderMatchDTOMapper;
import com.blockeng.admin.dto.PageDTO;
import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.EntrustOrderService;
import com.blockeng.admin.service.UserService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.dto.EntrustOrderMatchDTO;
import com.blockeng.framework.enums.OrderStatus;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 委托订单信息_币币交易 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/entrustOrder")
@Api(value = "委托订单_币币交易", description = "币币交易委托订单管理")
@Slf4j
public class EntrustOrderController {

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;


    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Log(value = "查询委托订单列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_entrust_order_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "marketId", value = "交易市场ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "交易方式(1买 2卖)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "priceType", value = "价格类型(1 市价 2 限价)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tradeType", value = "交易类型(1 开仓 2 平仓)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "交易状态(0未成交 1已成交 2已取消 4异常单)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "订单ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询委托订单列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<EntrustOrder> page,
                             String marketId, String type,
                             String priceType, String tradeType,
                             String status, String id,
                             String userId, String userName,
                             String mobile, String startTime,
                             String userType,
                             String endTime) {
//
        EntityWrapper<EntrustOrder> ew = new EntityWrapper<>();
        EntityWrapper<User> userEw = new EntityWrapper<>();
//        userEw.setParamAlias("otherEw");
        ew.setParamAlias("entrustEw");
        userEw.setParamAlias("userEw");
        boolean isUserNameLike = true;
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userName) && Strings.isNullOrEmpty(userId)) {
            if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("username", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    ew.eq("a.user_id", user.getId());
                } else {
                    isUserNameLike = false;
                }
            }
        }

        if (!Strings.isNullOrEmpty(mobile) && Strings.isNullOrEmpty(userId)) {
            if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("mobile", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    ew.eq("a.user_id", user.getId());
                } else {
                    isUserNameLike = false;
                }
            }
        }
//        if (!Strings.isNullOrEmpty(mobile)) {
//            userEw.eq("b.mobile", mobile);
//        }
//        if (!Strings.isNullOrEmpty(userType)) {
//            if (userType.equals("1")) {
//                ew.where("b.flag = 0");
//            } else {
//                ew.where("b.flag != 0");
//            }
//        }
        //为a表增加的查询
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("a.market_id", marketId);
        }
        if (!Strings.isNullOrEmpty(type)) {
            ew.eq("a.type", type);
        }
        if (!Strings.isNullOrEmpty(priceType)) {
            ew.eq("a.price_type", priceType);
        }
        if (!Strings.isNullOrEmpty(tradeType)) {
            ew.eq("a.trade_type", tradeType);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(id)) {
            ew.eq("a.id", id);
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("a.user_id", userId);
        }
//        if (!Strings.isNullOrEmpty(startTime)) {
//            ew.ge("a.created", startTime);
//        }

        int size = page.getSize();
        int total = 0;
        int current = (page.getCurrent() - 1) * size;

        List<EntrustOrder> entrustOrders = new ArrayList<>();

        if (isUserNameLike) {
            if (!ew.isEmptyOfWhere()) {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                }
                entrustOrders = entrustOrderService.selectListPageByOrder(ew, userEw, current, size);
                total = entrustOrderService.selectListPageByOrderCount(ew, userEw);
            } else if (!userEw.isEmptyOfWhere()) {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                }
                entrustOrders = entrustOrderService.selectListPageByUser(userEw, ew, current, size);
                total = entrustOrderService.selectListPageByUserCount(ew, userEw);
            } else {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                    entrustOrders = entrustOrderService.selectListPageByOrder(ew, userEw, current, size);
                    total = entrustOrderService.selectListPageCountInDays(ew, current, size);
                } else {
                    entrustOrders = entrustOrderService.selectListPageEmpty(current, size);
                    total = entrustOrderService.selectListPageCount();
                }
            }
        }

        if (entrustOrders.size() < size && page.getCurrent() <= 1) {
            total = entrustOrders.size();
        } else if (entrustOrders.size() > 15) {
            entrustOrders = entrustOrders.subList(0, 15);
        }
        return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(page.getCurrent()).setSize(size).setTotal(total).setRecords(entrustOrders));
    }

    @Log(value = "币币交易委托订单导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('trade_entrust_order_export')")
    @RequestMapping({"/exportList"})
    @ResponseBody
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "委托订单导出", notes = "委托订单导出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "marketId", value = "交易市场ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "交易方式(1买 2卖)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "priceType", value = "价格类型(1 市价 2 限价)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "tradeType", value = "交易类型(1 开仓 2 平仓)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "交易状态(0未成交 1已成交 2已取消 4异常单)", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "订单ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    public void exportList(@ApiIgnore Page<EntrustOrder> page,
                           String marketId, String type,
                           String priceType, String tradeType,
                           String status, String id,
                           String userType,
                           String userId, String userName,
                           String mobile, String startTime,
                           String endTime
            , HttpServletResponse response) throws Exception {
        //   ew.eq("market_type", 1); 数据库每天币币交易类型几乎没有 2018.8.24 导出根本查不出数据 加他何用?
        EntityWrapper<EntrustOrder> ew = new EntityWrapper<>();
        EntityWrapper<User> userEw = new EntityWrapper<>();
//        userEw.setParamAlias("otherEw");
        ew.setParamAlias("entrustEw");
        userEw.setParamAlias("userEw");
        boolean isUserNameLike = true;
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userName) && Strings.isNullOrEmpty(userId)) {
            if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("username", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    ew.eq("a.user_id", user.getId());
                } else {
                    isUserNameLike = false;
                }
            }
        }
        if (!Strings.isNullOrEmpty(userType)) {
            if (userType.equals("1")) {
                ew.where("b.flag = 0");
            } else {
                ew.where("b.flag != 0");

            }
        }
        if (!Strings.isNullOrEmpty(mobile) && Strings.isNullOrEmpty(userId)) {
            if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("mobile", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    ew.eq("a.user_id", user.getId());
                } else {
                    isUserNameLike = false;
                }
            }
        }

        //为a表增加的查询
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("a.market_id", marketId);
        }
        if (!Strings.isNullOrEmpty(type)) {
            ew.eq("a.type", type);
        }
        if (!Strings.isNullOrEmpty(priceType)) {
            ew.eq("a.price_type", priceType);
        }
        if (!Strings.isNullOrEmpty(tradeType)) {
            ew.eq("a.trade_type", tradeType);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(id)) {
            ew.eq("a.id", id);
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("a.user_id", userId);
        }
//        if (!Strings.isNullOrEmpty(startTime)) {
//            ew.ge("a.created", startTime);
//        }

        int size = 200000;
        int total = 0;
        int current = 0;

        List<EntrustOrder> entrustOrders = new ArrayList<>();

        if (isUserNameLike) {
            if (!ew.isEmptyOfWhere()) {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                }
                entrustOrders = entrustOrderService.selectListPageByOrder(ew, userEw, current, size);
                //   total = entrustOrderService.selectListPageByOrderCount(ew, userEw);
            } else if (!userEw.isEmptyOfWhere()) {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                }
                entrustOrders = entrustOrderService.selectListPageByUser(userEw, ew, current, size);
                //  total = entrustOrderService.selectListPageByUserCount(ew, userEw);
            } else {
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    ew.between("a.created", startTime, endTime);
                    entrustOrders = entrustOrderService.selectListPageByOrder(ew, userEw, current, size);
                    //      total = entrustOrderService.selectListPageCountInDays(ew, current, size);
                } else {
                    entrustOrders = entrustOrderService.selectListPageEmpty(current, size);
                    //       total = entrustOrderService.selectListPageCount();
                }
            }
        }
//        if (entrustOrders.size() < size && page.getCurrent() <= 1) {
//            total = entrustOrders.size();
//        } else if (entrustOrders.size() > 15) {
//            entrustOrders = entrustOrders.subList(0, 15);
//        }
        String[] header = {"订单ID", "用户ID", "用户名", "交易市场", "委托价格", "委托数量", "预计成交额", "已成交量", "手续费", "冻结金额", "交易方式", "状态", "委托时间"};
        String[] properties = {"idStr", "userIdStr", "userName", "marketName", "price", "volume", "predictTurnoverAmount", "dealStr", "fee", "freezeStr", "typeStr", "statusStr", "createdStr"};

        CellProcessor[] PROCESSORS = new CellProcessor[]{
                null,
                null,
                null,
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                null,
                null,
                null
        };
        String fileName = "币币交易委托.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, entrustOrders, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 后台撤单
     * @param orderId
     * @return
     */
    @PreAuthorize("hasAuthority('order_cancel')")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "撤销订单", notes = "撤销订单")
    @PostMapping("/cancelOrder/{orderId}")
    public Object cancelEntrustOrder(@PathVariable("orderId")String orderId) {
        if (Long.valueOf(orderId) <= 0L) {
            log.error("撤单失败，订单号错误，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
        EntrustOrder entrustOrder = entrustOrderService.selectById(Long.valueOf(orderId));
        if (entrustOrder == null) {
            log.error("撤单失败，委托订单不存在，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }

        if (entrustOrder.getStatus()==OrderStatus.DEAL.getCode()){

            return Response.err(50010,"订单已成交，无法撤销");
        }else if (entrustOrder.getStatus()==OrderStatus.CANCEL.getCode()){

            return Response.err(50010,"订单已撤销，请勿重复操作");
        }else if (entrustOrder.getStatus()==OrderStatus.MATCH_ABNORMAL.getCode()){

            return Response.err(50010,"订单异常，无法撤销");
        }
        EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);

        // 推送撮合队列
        entrustOrderService.startCancel(Long.valueOf(orderId));
        entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
        String json = new Gson().toJson(entrustOrderDTO);
        rabbitTemplate.convertAndSend("order.in", json);
        return Response.ok();
    }

    /**
     * 批量撤单
     * @param orderIds
     * @return
     */
    @PreAuthorize("hasAuthority('order_cancel')")
    @PostMapping("/batchCancelOrder")
    public Object batchCancelEntrustOrder(@RequestBody Long[] orderIds) {

        if (orderIds==null || orderIds.length<=0){
            ResultMap.getFailureResult("orderId不能为空!");
        }
        for (Long orderId : orderIds) {
            EntrustOrder entrustOrder = entrustOrderService.selectById(orderId);
            if (entrustOrder==null){
                log.error("orderId:{} 不存在",orderId);
                continue;
            }
            if(entrustOrder.getStatus()!=OrderStatus.PENDING.getCode()){
                continue;
            }
            entrustOrderService.startCancel(Long.valueOf(orderId));
            EntrustOrderMatchDTO entrustOrderDTO = EntrustOrderMatchDTOMapper.INSTANCE.from(entrustOrder);
            entrustOrderDTO.setStatus(OrderStatus.CANCEL.getCode());
            String json = new Gson().toJson(entrustOrderDTO);
            rabbitTemplate.convertAndSend("order.in", json);
        }
        return Response.ok();
    }
}