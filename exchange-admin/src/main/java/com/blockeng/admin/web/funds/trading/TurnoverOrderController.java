package com.blockeng.admin.web.funds.trading;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.PageDTO;
import com.blockeng.admin.dto.TurnOverOrderTheCountDTO;
import com.blockeng.admin.dto.TurnOverOrderTheTotalCountDTOPage;
import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.admin.entity.TurnoverOrder;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.EntrustOrderService;
import com.blockeng.admin.service.TurnoverOrderService;
import com.blockeng.admin.service.UserService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 成交订单 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-5-16 16:48:09
 */
@Slf4j
@RestController
@RequestMapping("/turnoverOrder")
@Api(value = "成交订单", description = "成交订单管理")
public class TurnoverOrderController {

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private UserService userService;

    @Autowired
    private EntrustOrderService entrustOrderService;


    //1真实用户，2是机器人
    @Log(value = "币币交易统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_deal_order_count_query')")
    @GetMapping("turnoverOrderCount")
    public Object turnoverOrderCount(String current, String size, String marketId, String userId, String userType, String startTime, String endTime) {
        EntityWrapper<TurnOverOrderTheCountDTO> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("c.id", marketId);
        }
        if (!Strings.isNullOrEmpty(userType)) {
            if (userType.equals("0")) {
                ew.eq("a.flag", 0);
            } else if (userType.equals("1")) {
                ew.andNew().eq("a.flag", 1).or().eq("a.flag", 2);
            }
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }

        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("a.id", userId);
        }

        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            ew.between("b.created", startTime, endTime);
        }
        int s = Integer.valueOf(size);
        int t = 0;
        int c = (Integer.valueOf(current).intValue() - 1) * s;
        List<TurnOverOrderTheCountDTO> list = turnoverOrderService.selectTheCountDTO(c, s, ew);
        list.forEach(theCountDTO -> {
            if (theCountDTO.getUserType().intValue() == 0) {
                theCountDTO.setUserTypeStr("真实用户");
            } else {
                theCountDTO.setUserTypeStr("机器人");
            }
        });
        t = turnoverOrderService.selectTheCountDTOCount(ew);

        TurnOverOrderTheTotalCountDTOPage turnOverOrderTheTotalCountDTO = turnoverOrderService.selectTheTotalCountDTO(ew);

        turnOverOrderTheTotalCountDTO.setRecords(list).setTotal(t).setCurrent(c + 1).setSize(s);

        return ResultMap.getSuccessfulResult(turnOverOrderTheTotalCountDTO);
    }

    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "userId", value = "用戶ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userType", value = "用户类型:0 真实用户 1 机器人用户", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "marketId", value = "交易市场ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币币交易导出", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = OutputStream.class),
            @ApiResponse(code = 1, message = "失败")

    })
    @Log(value = "币币交易统计导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAnyAuthority('trade_deal_order_count_export')")
    @GetMapping("exportTurnoverOrderCount")
    public void exportTurnoverOrderCount(String marketId, String userId, String userType, String startTime, String endTime, HttpServletResponse response) {
        EntityWrapper<TurnOverOrderTheCountDTO> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("c.id", marketId);
        }
        if (!Strings.isNullOrEmpty(userType)) {
            if (userType.equals("0")) {
                ew.eq("a.flag", 0);
            } else if (userType.equals("1")) {
                ew.andNew().eq("a.flag", 1).or().eq("a.flag", 2);
            }
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("a.id", userId);
        }
        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            ew.between("b.created", startTime, endTime);
        }
        int s = 200000;
        int c = 0;
        List<TurnOverOrderTheCountDTO> list = turnoverOrderService.selectTheCountDTO(c, s, ew);

        TurnOverOrderTheTotalCountDTOPage turnOverOrderTheTotalCountDTO = turnoverOrderService.selectTheTotalCountDTO(ew);
        String[] header = {"用户ID", "交易市场", "成交量", "成交额", "买房手续费", "卖方手续费", "订单数", "用户类型"};
        String[] properties = {"userId", "marketName", "turnOverNumber", "turnOverAmount", "buyFee", "sellFee", "orderNum", "userType"};
        CellProcessor[] PROCESSORS = new CellProcessor[]{
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
                null
        };
        String fileName = "成交订单.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Log(value = "查询成交订单列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_deal_order_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "订单ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用戶ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "订单类型:1 币币交易 2 创新交易", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "marketId", value = "交易市场ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tradeType", value = "交易方式(1买 2卖)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询成交订单列表", httpMethod = "GET")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = TurnoverOrder.class),
            @ApiResponse(code = 1, message = "失败")

    })
    public Object selectPage(@ApiIgnore Page<TurnoverOrder> page,
                             String orderId, String userId, String type,
                             String marketId, String userName, String mobile, String userType,
                             String tradeType, String startTime, String endTime) {
        EntityWrapper<TurnoverOrder> ew = new EntityWrapper<>();
        EntityWrapper<TurnoverOrder> ow = new EntityWrapper<>();
        ew.setParamAlias("turnover1Ew");
        ow.setParamAlias("turnover2Ew");

//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("a.market_id", marketId);
        }
        long uid = 0;
        int size = page.getSize();
        int total = 0;
        int current = (page.getCurrent() - 1) * size;
        List<TurnoverOrder> turnoverOrders = new ArrayList<>();
        EntrustOrder entrustOrder = null;
        // 订单id优先
        if (!Strings.isNullOrEmpty(orderId)) {
            EntityWrapper<EntrustOrder> ewOrder = new EntityWrapper<>();
            ewOrder.eq("id", orderId);
            entrustOrder = entrustOrderService.selectOne(ewOrder);
            if (null != entrustOrder) { //如果订单存在，才继续往下查，否则没有数据
                long uidInOrder = entrustOrder.getUserId();
                int tType = entrustOrder.getType();
                if (!Strings.isNullOrEmpty(userId)) { //如果有userId条件，那么判断userId是不是订单的发起人
                    if (uidInOrder == Long.valueOf(userId).longValue()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                        fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                        fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                        fillTimeCondition(ew, startTime, endTime);
                        turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                        total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                        uid = uidInOrder;
                    } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                        fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                        if (!Strings.isNullOrEmpty(tradeType)) {
                            fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", Long.valueOf(userId).longValue());
                        } else {
                            fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), Long.valueOf(userId).longValue());
                        }
                        fillTimeCondition(ew, startTime, endTime);
                        turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                        total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                        uid = uidInOrder;
                    }
                } else if (!Strings.isNullOrEmpty(mobile)) {
                    EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                    entityWrapper.eq("mobile", mobile);
                    User user = userService.selectOne(entityWrapper);
                    if (null != user) {
                        if (uidInOrder == user.getId()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                            uid = uidInOrder;
                        } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            if (!Strings.isNullOrEmpty(tradeType)) {
                                fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", user.getId());
                            } else {
                                fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), user.getId());
                            }
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                            uid = uidInOrder;
                        }
                    }
                } else if (!Strings.isNullOrEmpty(userName)) { //findByUser
                    EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                    entityWrapper.like("username", userName);
                    User user = userService.selectOne(entityWrapper);
                    if (null != user) {
                        if (uidInOrder == user.getId()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                            uid = uidInOrder;
                        } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            if (!Strings.isNullOrEmpty(tradeType)) {
                                fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", user.getId());
                            } else {
                                fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), user.getId());
                            }
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                            uid = uidInOrder;
                        }
                    }
                } else {
                    fillOrderCondition(ew, 0, Long.valueOf(orderId).longValue());
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                    total = turnoverOrderService.selectListPageByOrderCount(current, size, ew, ow);
                }
            }
            // id优先，mobile其次，用户名最次
        } else if (!Strings.isNullOrEmpty(userId) || !Strings.isNullOrEmpty(mobile) || !Strings.isNullOrEmpty(userName)) {
            if (!Strings.isNullOrEmpty(userId)) {
                uid = Long.valueOf(userId);
                fillTradeTypeCondition(ew, tradeType, uid);
                fillTimeCondition(ew, startTime, endTime);
                turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
                total = turnoverOrderService.selectListPageByUserCount(current, size, ew, ow);
            } else if (!Strings.isNullOrEmpty(mobile)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("mobile", mobile);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    uid = user.getId();
                    fillTradeTypeCondition(ew, tradeType, uid);
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
                    total = turnoverOrderService.selectListPageByUserCount(current, size, ew, ow);
                }
            } else if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("username", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    uid = user.getId();
                    fillTradeTypeCondition(ew, tradeType, uid);
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
                    total = turnoverOrderService.selectListPageByUserCount(current, size, ew, ow);
                }
            }
        } else {
            fillTimeCondition(ew, startTime, endTime);
            turnoverOrders = turnoverOrderService.selectListPage(current, size, ew, ow);
            total = turnoverOrderService.selectListPageCount(current, size, ew, ow);
        }

        if (turnoverOrders.size() > page.getSize()) {
            turnoverOrders = turnoverOrders.subList(0, 15);
        }

        final long uidFinal = uid;
        final EntrustOrder entrustOrderFinal = entrustOrder;
        if (uidFinal == 0 && Strings.isNullOrEmpty(orderId)) {

            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                } else {
                    turnoverOrder.setTradeType(4);
                }
            });
        } else if (!Strings.isNullOrEmpty(orderId)) {

            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                } else {
                    if (!Strings.isNullOrEmpty(tradeType) && "1".equals(tradeType)) {
                        turnoverOrder.setTradeType(1);
                    } else if (!Strings.isNullOrEmpty(tradeType) && "2".equals(tradeType)) {
                        turnoverOrder.setTradeType(2);
                    } else {
                        turnoverOrder.setTradeType(4);
                    }
                }
            });
        } else {
            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                    turnoverOrder.setOrderId(turnoverOrder.getSellOrderId());
                } else if (turnoverOrder.getBuyUserId() == uidFinal) {
                    turnoverOrder.setTradeType(1);
                    turnoverOrder.setOrderId(turnoverOrder.getBuyOrderId());
                } else if (turnoverOrder.getSellUserId() == uidFinal) {
                    turnoverOrder.setTradeType(2);
                    turnoverOrder.setOrderId(turnoverOrder.getSellOrderId());
                }
            });
        }
        return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(page.getCurrent()).setSize(size).setTotal(total).setRecords(turnoverOrders));
    }

    /**
     * 填充订单条件
     *
     * @param ew      查询条件
     * @param type    订单类型
     * @param orderId 订单id
     */
    private void fillOrderCondition(EntityWrapper<TurnoverOrder> ew, int type, long orderId) {
        if (type == 1) {
            ew.eq("a.buy_order_id", orderId);
        } else if (type == 2) {
            ew.eq("a.sell_order_id", orderId);
        } else {
            ew.andNew().eq("a.sell_order_id", orderId).or().eq("a.buy_order_id", orderId);
        }
    }

    /**
     * 填充时间条件
     *
     * @param ew        查询条件
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    private void fillTimeCondition(EntityWrapper<TurnoverOrder> ew, String startTime, String endTime) {
        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
            ew.andNew().between("a.created", startTime, endTime);
        }
    }

    /**
     * 根据市场类型填充查询条件
     *
     * @param ew        查询条件
     * @param tradeType 交易类型
     * @param uid       用户id
     */
    private void fillTradeTypeCondition(EntityWrapper<TurnoverOrder> ew, String tradeType, long uid) {
        if ("2".equals(tradeType)) {
            ew.eq("a.sell_user_id", uid);
        } else if ("1".equals(tradeType)) {
            ew.eq("a.buy_user_id", uid);
        } else {
            ew.andNew().eq("a.sell_user_id", uid).or().eq("a.buy_user_id", uid);
        }
    }


    @Log(value = "成交订单导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('trade_deal_order_export')")
    @RequestMapping({"/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "成交订单导出", notes = "成交订单导出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "订单类型:1 币币交易 2 创新交易", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "marketId", value = "交易市场ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用戶ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "买家", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "tradeType", value = "交易方式(1买 2卖,3自交易)", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })

    public void export(@ApiIgnore Page<TurnoverOrder> page, String type, String orderId,
                       String marketId, String userId, String userName, String mobile, String userType,
                       String tradeType, String startTime, String endTime
            , HttpServletResponse response) throws Exception {
        EntityWrapper<TurnoverOrder> ew = new EntityWrapper<>();
        EntityWrapper<TurnoverOrder> ow = new EntityWrapper<>();
        ew.setParamAlias("turnover1Ew");
        ow.setParamAlias("turnover2Ew");

//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(marketId)) {
            ew.eq("a.market_id", marketId);
        }

        long uid = 0;
        int size = 200000;
        int current = 0;
        List<TurnoverOrder> turnoverOrders = new ArrayList<>();
        EntrustOrder entrustOrder = null;
        // 订单id优先
        if (!Strings.isNullOrEmpty(orderId)) {
            EntityWrapper<EntrustOrder> ewOrder = new EntityWrapper<>();
            ewOrder.eq("id", orderId);
            entrustOrder = entrustOrderService.selectOne(ewOrder);
            if (null != entrustOrder) { //如果订单存在，才继续往下查，否则没有数据
                long uidInOrder = entrustOrder.getUserId();
                int tType = entrustOrder.getType();
                if (!Strings.isNullOrEmpty(userId)) { //如果有userId条件，那么判断userId是不是订单的发起人
                    if (uidInOrder == Long.valueOf(userId).longValue()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                        fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                        fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                        fillTimeCondition(ew, startTime, endTime);
                        turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                        uid = uidInOrder;
                    } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                        fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                        if (!Strings.isNullOrEmpty(tradeType)) {
                            fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", Long.valueOf(userId).longValue());
                        } else {
                            fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), Long.valueOf(userId).longValue());
                        }
                        fillTimeCondition(ew, startTime, endTime);
                        turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                        uid = uidInOrder;
                    }
                } else if (!Strings.isNullOrEmpty(mobile)) {
                    EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                    entityWrapper.eq("mobile", mobile);
                    User user = userService.selectOne(entityWrapper);
                    if (null != user) {
                        if (uidInOrder == user.getId()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            uid = uidInOrder;
                        } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            if (!Strings.isNullOrEmpty(tradeType)) {
                                fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", user.getId());
                            } else {
                                fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), user.getId());
                            }
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            uid = uidInOrder;
                        }
                    }
                } else if (!Strings.isNullOrEmpty(userName)) { //findByUser
                    EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                    entityWrapper.like("username", userName);
                    User user = userService.selectOne(entityWrapper);
                    if (null != user) {
                        if (uidInOrder == user.getId()) { //如果是发起人，那么按照订单的买卖来决定userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            fillTradeTypeCondition(ew, String.valueOf(tType), uidInOrder);
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            uid = uidInOrder;
                        } else { //如果不是发起人，那么按照订单买卖相反的userId的条件
                            fillOrderCondition(ew, tType, Long.valueOf(orderId).longValue());
                            if (!Strings.isNullOrEmpty(tradeType)) {
                                fillTradeTypeCondition(ew, "1".equals(tradeType) ? "2" : "1", user.getId());
                            } else {
                                fillTradeTypeCondition(ew, String.valueOf(tType == 1 ? 2 : 1), user.getId());
                            }
                            fillTimeCondition(ew, startTime, endTime);
                            turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                            uid = uidInOrder;
                        }
                    }
                } else {
                    fillOrderCondition(ew, 0, Long.valueOf(orderId).longValue());
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByOrder(current, size, ew, ow);
                }
            }
            // id优先，mobile其次，用户名最次
        } else if (!Strings.isNullOrEmpty(userId) || !Strings.isNullOrEmpty(mobile) || !Strings.isNullOrEmpty(userName)) {
            if (!Strings.isNullOrEmpty(userId)) {
                uid = Long.valueOf(userId);
                fillTradeTypeCondition(ew, tradeType, uid);
                fillTimeCondition(ew, startTime, endTime);
                turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
            } else if (!Strings.isNullOrEmpty(mobile)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.eq("mobile", mobile);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    uid = user.getId();
                    fillTradeTypeCondition(ew, tradeType, uid);
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
                }
            } else if (!Strings.isNullOrEmpty(userName)) {
                EntityWrapper<User> entityWrapper = new EntityWrapper<>();
                entityWrapper.like("username", userName);
                User user = userService.selectOne(entityWrapper);
                if (null != user) {
                    uid = user.getId();
                    fillTradeTypeCondition(ew, tradeType, uid);
                    fillTimeCondition(ew, startTime, endTime);
                    turnoverOrders = turnoverOrderService.selectListPageByUser(current, size, ew, ow);
                }
            }
        } else {//没条件
            fillTimeCondition(ew, startTime, endTime);
            turnoverOrders = turnoverOrderService.selectListPage(current, size, ew, ow);
        }


        final long uidFinal = uid;
        final EntrustOrder entrustOrderFinal = entrustOrder;
        if (uidFinal == 0 && Strings.isNullOrEmpty(orderId)) {

            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                } else {
                    if (!Strings.isNullOrEmpty(tradeType) && "1".equals(tradeType)) {
                        turnoverOrder.setTradeType(1);
                    } else if (!Strings.isNullOrEmpty(tradeType) && "2".equals(tradeType)) {
                        turnoverOrder.setTradeType(2);
                    } else {
                        turnoverOrder.setTradeType(4);
                    }
                }
            });
        } else if (!Strings.isNullOrEmpty(orderId)) {

            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                } else {
                    turnoverOrder.setTradeType(entrustOrderFinal.getType());
                }
                turnoverOrder.setOrderId(Long.valueOf(orderId));
            });
        } else {
            turnoverOrders.stream().forEach(turnoverOrder -> {
                if (turnoverOrder.getSellUserId().equals(turnoverOrder.getBuyUserId())) {
                    turnoverOrder.setTradeType(3);
                    turnoverOrder.setOrderId(turnoverOrder.getSellOrderId());
                } else if (turnoverOrder.getBuyUserId() == uidFinal) {
                    turnoverOrder.setTradeType(1);
                    turnoverOrder.setOrderId(turnoverOrder.getBuyOrderId());
                } else if (turnoverOrder.getSellUserId() == uidFinal) {
                    turnoverOrder.setTradeType(2);
                    turnoverOrder.setOrderId(turnoverOrder.getSellOrderId());
                }
            });
        }

        String[] header = {"ID", "交易市场", "买方订单ID", "买方用户ID", "卖方订单ID", "卖方用户ID", "交易方式", "成交价", "成交量", "成交额", "成交买入手续费", "成交卖出手续费", "成交时间"};
        String[] properties = {"idStr", "marketName", "buyOrderId", "buyUserId", "sellOrderId", "sellUserId", "tradeTypeStr", "price", "volume", "amount", "dealBuyFee", "dealSellFee", "createdStr"};
        CellProcessor[] PROCESSORS = new CellProcessor[]{
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                }, //ID
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                }, //交易市场
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },//买方订单
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },//买方用户
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },//卖方订单
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },//卖方用户
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },//订单类型
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
                },//成交价
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
                },//成交量
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
                },//成交额
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
                },//入手续费
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
                },//出手续费
                null //时间
        };
        String fileName = "成交订单.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, turnoverOrders, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
