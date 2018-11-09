package com.blockeng.admin.web.funds.account;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.PageDTO;
import com.blockeng.admin.entity.AccountDetail;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountDetailService;
import com.blockeng.admin.service.UserService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资金账户流水 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-16
 */
@RestController
@RequestMapping("/accountDetail")
@Api(value = "资金账户流水", description = "资金账户流水管理")
public class AccountDetailController {

    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private UserService userService;
//
//    @GetMapping("test")
//    public void aaa(){
//
//        AccountDetail accountDetail = new AccountDetail();
//        accountDetail.setId(System.currentTimeMillis());
//        accountDetail.setAccountId(System.currentTimeMillis());
//        accountDetail.setAmount(new BigDecimal(0));
//        accountDetail.setBusinessType("1");
//        accountDetail.setCoinId(System.currentTimeMillis());
//        accountDetail.setDirection(1);
//        accountDetail.setFee(new BigDecimal(0));
//        accountDetail.setOrderId(System.currentTimeMillis());
//        accountDetail.setRefAccountId(System.currentTimeMillis());
//        accountDetail.setRemark("");
//        accountDetail.setUserId(System.currentTimeMillis());
//        accountDetail.setUserName("2");
//        accountDetailService.insert(accountDetail);
//
//    }

    @GetMapping("test")
    public void aaa() {

        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setId(System.currentTimeMillis());
        accountDetail.setAccountId(System.currentTimeMillis());
        accountDetail.setAmount(new BigDecimal(0));
        accountDetail.setBusinessType("1");
        accountDetail.setCoinId(System.currentTimeMillis());
        accountDetail.setDirection(1);
        accountDetail.setFee(new BigDecimal(0));
        accountDetail.setOrderId(System.currentTimeMillis());
        accountDetail.setRefAccountId(System.currentTimeMillis());
        accountDetail.setRemark("");
        accountDetail.setUserId(System.currentTimeMillis());
        accountDetail.setUserName("2");
        accountDetailService.insert(accountDetail);

    }


    @Log(value = "按条件分页查询资金账户流水列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('account_detail_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "accountId", value = "账户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = " 用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注:充值(recharge_into) 提现审核通过(withdrawals_out) 下单(order_create) 成交(order_turnover) 成交手续费(order_turnover_poundage)  撤单(order_cancel)  注册奖励(bonus_register) 提币冻结解冻(withdrawals) 充人民币(recharge) 提币手续费(withdrawals_poundage)    兑换(cny_btcx_exchange) 奖励充值(bonus_into) 奖励冻结(bonus_freeze)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amountStart", value = "起始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amountEnd", value = "截止金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询资金账户流水列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<AccountDetail> page,
                             String accountId, String userId,
                             String coinId, String userName, String mobile,
                             String remark, String amountStart, String amountEnd,
                             String userType,
                             String startTime, String endTime) {
        EntityWrapper<AccountDetail> accountEw = new EntityWrapper<>();

        accountEw.setParamAlias("accountEx");

        EntityWrapper<User> userEw = new EntityWrapper<>();

        userEw.setParamAlias("userEx");
        boolean isUserNameLike = true;
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(accountId)) {
            accountEw.eq("a.account_id", accountId);
        }
        if (!Strings.isNullOrEmpty(coinId)) {

            accountEw.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(userId)) {

            accountEw.eq("a.user_id", userId);
        }

        if (!Strings.isNullOrEmpty(remark)) {

            accountEw.eq("a.business_type", remark);
        }
        if (!Strings.isNullOrEmpty(amountStart)) {

            accountEw.ge("a.amount", amountStart);
        }
        if (!Strings.isNullOrEmpty(amountEnd)) {

            accountEw.le("a.amount", amountEnd);
        }
        if (!Strings.isNullOrEmpty(userName) && Strings.isNullOrEmpty(userId)) {
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("username", userName);
            User user = userService.selectOne(entityWrapper);
            //  userEw.like("b.username", userName);
            if (null != user) {
                accountEw.eq("a.user_id", user.getId());
            } else {
                isUserNameLike = false;
            }
        }

        if (!Strings.isNullOrEmpty(mobile) && Strings.isNullOrEmpty(userId)) {
            //    userEw.eq("b.mobile", mobile);
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("mobile", userName);
            User user = userService.selectOne(entityWrapper);
            //  userEw.like("b.username", userName);
            if (null != user) {
                accountEw.eq("a.user_id", user.getId());
            } else {
                isUserNameLike = false;
            }
        }

//        if (!Strings.isNullOrEmpty(userType)) {
//            if (userType.equals("1")) {
//                accountEw.where(" b.flag = 0");
//            } else {
//                accountEw.where(" b.flag != 0");
//            }
//        }


        int size = page.getSize();
        int total = 0;
        int current = (page.getCurrent() - 1) * size;

        List<AccountDetail> accountDetails = new ArrayList<>();

        boolean accountEwEmptyOfWhere = accountEw.isEmptyOfWhere();

        boolean userEwEmptyOfWhere = userEw.isEmptyOfWhere();

        if (isUserNameLike) {

            if (!accountEwEmptyOfWhere) {//用account作为主要查询条件
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                }
                accountDetails = accountDetailService.selectListPageFromAccount(current, size, accountEw, userEw);
            } else if (!userEwEmptyOfWhere) {//用user作为主要查询条件
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                }
                accountDetails = accountDetailService.selectListPageFromUser(current, size, userEw, accountEw);
            } else {//直接搜索,无任何查询条件

                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                    accountDetails = accountDetailService.selectListPageFromAccount(current, size, accountEw, userEw);
                } else {
                    accountDetails = accountDetailService.selectListPageEmpty(current, size);
                }
            }
            if (!CollectionUtils.isEmpty(accountDetails)) {
                if (accountDetails.size() < size && page.getCurrent() <= 1) {
                    total = accountDetails.size();
                } else {
                    if (accountDetails.size() > 15)
                        accountDetails = accountDetails.subList(0, 15);

                    if (!accountEwEmptyOfWhere)
                        total = accountDetailService.selectListPageCountFromAccount(accountEw, userEw);
                    else if (!userEwEmptyOfWhere)
                        total = accountDetailService.selectListPageCountFromUser(userEw, accountEw);
                    else {
                        if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                            total = accountDetailService.selectListPageEmptyInDaysCount(accountEw, userEw);
                        } else {
                            total = accountDetailService.selectListPageCount();
                        }
                    }
                }
            }
        }
        return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(page.getCurrent()).setSize(size).setTotal(total).setRecords(accountDetails));
    }


    @Log(value = "导出资金账户流水", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('account_detail_export')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "导出资金账户流水", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "账户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = " 用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "备注:充值(recharge_into) 提现审核通过(withdrawals_out) 下单(order_create) 成交(order_turnover) 成交手续费(order_turnover_poundage)  撤单(order_cancel)  注册奖励(bonus_register) 提币冻结解冻(withdrawals) 充人民币(recharge) 提币手续费(withdrawals_poundage)    兑换(cny_btcx_exchange) 奖励充值(bonus_into) 奖励冻结(bonus_freeze)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amountStart", value = "起始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "amountEnd", value = "截止金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @RequestMapping({"/exportList"})
    public void export(@ApiIgnore Page<AccountDetail> page,
                       String accountId, String userId,
                       String coinId, String userName, String mobile,
                       String userType,
                       String remark, String amountStart, String amountEnd,
                       String startTime, String endTime
            , HttpServletResponse response) {
        EntityWrapper<AccountDetail> accountEw = new EntityWrapper<>();

        accountEw.setParamAlias("accountEx");

        EntityWrapper<User> userEw = new EntityWrapper<>();

        userEw.setParamAlias("userEx");
        boolean isUserNameLike = true;
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
//
//        if (!Strings.isNullOrEmpty(userType)) {
//            if (userType.equals("1")) {
//                accountEw.andNew(" b.flag = 0");
//            } else {
//                accountEw.andNew(" b.flag != 0");
//            }
//
//        }

        if (!Strings.isNullOrEmpty(accountId)) {
            accountEw.eq("a.account_id", accountId);
        }
        if (!Strings.isNullOrEmpty(coinId)) {

            accountEw.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(userId)) {

            accountEw.eq("a.user_id", userId);
        }

        if (!Strings.isNullOrEmpty(remark)) {

            accountEw.eq("a.business_type", remark);
        }
        if (!Strings.isNullOrEmpty(amountStart)) {

            accountEw.ge("a.amount", amountStart);
        }
        if (!Strings.isNullOrEmpty(amountEnd)) {

            accountEw.le("a.amount", amountEnd);
        }
        if (!Strings.isNullOrEmpty(userName) && Strings.isNullOrEmpty(userId)) {
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("username", userName);
            User user = userService.selectOne(entityWrapper);
            //  userEw.like("b.username", userName);
            if (null != user) {
                accountEw.eq("a.user_id", user.getId());
            } else {
                isUserNameLike = false;
            }
        }

        if (!Strings.isNullOrEmpty(mobile) && Strings.isNullOrEmpty(userId)) {
            //    userEw.eq("b.mobile", mobile);
            EntityWrapper<User> entityWrapper = new EntityWrapper<>();
            entityWrapper.like("mobile", userName);
            User user = userService.selectOne(entityWrapper);
            //  userEw.like("b.username", userName);
            if (null != user) {
                accountEw.eq("a.user_id", user.getId());
            } else {
                isUserNameLike = false;
            }
        }
        int size = 200000;
        int total = 0;
        int current = 0;

        List<AccountDetail> accountDetails = new ArrayList<>();
        boolean accountEwEmptyOfWhere = accountEw.isEmptyOfWhere();

        boolean userEwEmptyOfWhere = userEw.isEmptyOfWhere();

        if (isUserNameLike) {
            if (!accountEwEmptyOfWhere) {//用account作为主要查询条件
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                }
                accountDetails = accountDetailService.selectListPageFromAccount(current, size, accountEw, userEw);
            } else if (!userEwEmptyOfWhere) {//用user作为主要查询条件
                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                }
                accountDetails = accountDetailService.selectListPageFromUser(current, size, userEw, accountEw);
            } else {//直接搜索,无任何查询条件

                if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
                    accountEw.between("a.created", startTime, endTime);
                    accountDetails = accountDetailService.selectListPageFromAccount(current, size, accountEw, userEw);
                } else {
                    accountDetails = accountDetailService.selectListPageEmpty(current, size);
                }
            }
        }
//        if (!CollectionUtils.isEmpty(accountDetails)) {
//            if (accountDetails.size() < size && page.getCurrent() <= 1) {
//                total = accountDetails.size();
//            } else {
//             //   accountDetails = accountDetails.subList(0, 15);
//                if (!accountEwEmptyOfWhere)
//                    total = accountDetailService.selectListPageCountFromAccount(accountEw, userEw);
//                else if (!userEwEmptyOfWhere)
//                    total = accountDetailService.selectListPageCountFromUser(userEw, accountEw);
//                else {
//                    if (!Strings.isNullOrEmpty(startTime) && !Strings.isNullOrEmpty(endTime)) {
//                        total = accountDetailService.selectListPageEmptyInDaysCount(accountEw, userEw);
//                    } else {
//                        total = accountDetailService.selectListPageCount();
//                    }
//                }
//            }
//        }


        String[] header = {"ID", "账户ID", "关联账户ID", "用户名", "币种名称", "金额", "收付类型", "业务类型", "关联订单号", "发生时间", "备注"};
        String[] properties = {"idStr", "accountIdStr", "refAccountIdStr", "userName", "coinName", "amount", "directionStr", "businessTypeStr", "orderIdStr", "createdStr", "remark"};
        CellProcessor[] PROCESSORS = new CellProcessor[]{
                null,
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
                null,
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                null,
                null
        };
        String fileName = "资金流水.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, accountDetails, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
