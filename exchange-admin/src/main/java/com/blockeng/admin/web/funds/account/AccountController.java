package com.blockeng.admin.web.funds.account;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AccountDTO;
import com.blockeng.admin.dto.PageDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.entity.User;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.UserService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.exception.AccountException;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 账户资金记录 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/account")
@Api(value = "资金账户记录", description = "账户资金管理")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('account_query')")
    @Log(value = "查询账户资金列表", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "真实姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recAddr", value = "钱包地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态:1正常 2冻结", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startAmount", value = "起始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endAmount", value = "结束金额", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询账户资金列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<Account> page,
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "userName", defaultValue = "") String userName,
                             @RequestParam(value = "mobile", defaultValue = "") String mobile,
                             @RequestParam(value = "status", defaultValue = "") String status,
                             @RequestParam(value = "startAmount", defaultValue = "") String startAmount,
                             @RequestParam(value = "endAmount", defaultValue = "") String endAmount) {

        List<Long> userIds = new ArrayList<>();
        EntityWrapper<Account> accountEw = new EntityWrapper<>();
        EntityWrapper<User> userEw = new EntityWrapper<>();
        accountEw.setParamAlias("accountEw");
        userEw.setParamAlias("userEw");

        if (!Strings.isNullOrEmpty(userId)) {
            userEw.eq("b.id", userId);
        }
        if (!Strings.isNullOrEmpty(userName)) {
            userEw.eq("b.username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            userEw.eq("b.mobile", mobile);
        }


        if (!Strings.isNullOrEmpty(coinId)) {
            accountEw.eq("a.coin_id", Long.parseLong(coinId));
        }

        if (!CollectionUtils.isEmpty(userIds)) {
            accountEw.in("a.user_id", userIds);
        }
        if (!Strings.isNullOrEmpty(status)) {
            accountEw.eq("a.status", Integer.parseInt(status));
        }
        if (!Strings.isNullOrEmpty(startAmount)) {
            accountEw.ge("a.balance_amount", startAmount);
        }
        if (!Strings.isNullOrEmpty(endAmount)) {
            accountEw.le("a.balance_amount", endAmount);
        }


        int size = page.getSize();
        int total = 0;
        int current = (page.getCurrent() - 1) * size;
        List<Account> accountList = null;
        boolean accountEwEmptyOfWhere = accountEw.isEmptyOfWhere();

        boolean userEwEmptyOfWhere = userEw.isEmptyOfWhere();

        if (!accountEwEmptyOfWhere) {//用account作为主要查询条件

            accountList = accountService.selectListPageFromAccount(current, size, accountEw, userEw);
        } else if (!userEwEmptyOfWhere) {//用user作为主要查询条件

            accountList = accountService.selectListPageFromUser(current, size, userEw, accountEw);
        } else {//直接搜索,无任何查询条件

            accountList = accountService.selectListPageEmpty(current, size);
        }
        if (!CollectionUtils.isEmpty(accountList)) {
            if (accountList.size() < size && page.getCurrent() <= 1) {
                total = accountList.size();
            } else {
                if (!accountEwEmptyOfWhere)
                    total = accountService.selectListPageCountFromAccount(accountEw, userEw);
                else if (!userEwEmptyOfWhere)
                    total = accountService.selectListPageCountFromUser(userEw, accountEw);
                else
                    total = accountService.selectListPageCount();
            }
        }

        return ResultMap.getSuccessfulResult(new PageDTO().setCurrent(page.getCurrent()).setSize(size).setTotal(total).setRecords(accountList));
    }

    @Log(value = "资金状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('account_status_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "资金状态设置(status:1解冻 2冻结)")
    @RequestMapping(path = "/setStatus", method = RequestMethod.POST)
    public Object setStatus(@RequestBody Account account) {
        Account oriAccount = accountService.selectById(account.getId());
        if (null == oriAccount) {
            return ResultMap.getFailureResult("该记录不存在!");
        }
        if (oriAccount.getStatus() > 2
                || oriAccount.getStatus() < 1
                || oriAccount.getStatus() == null) {
            return ResultMap.getFailureResult("输入参数有误!");
        }

        if (oriAccount.getStatus().equals(account.getStatus())) {
            return ResultMap.getFailureResult("当前状态不能执行此操作!");
        }

        Account newAccount = new Account();
        newAccount.setId(account.getId());
        newAccount.setStatus(account.getStatus());
        //乐观锁更新
        newAccount.setVersion(oriAccount.getVersion());
        if (accountService.updateById(newAccount)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "账户资金导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('account_export')")
    @RequestMapping({"/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "账户资金导出", notes = "账户资金导出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "真实姓名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "recAddr", value = "钱包地址", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态:1正常 2冻结", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startAmount", value = "起始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endAmount", value = "结束金额", dataType = "String", paramType = "query"),
    })
    public void export(@ApiIgnore Page<Account> page,
                       String coinId, String userId,
                       String userName, String mobile,
                       String recAddr, String status,
                       String startAmount, String endAmount
            , HttpServletResponse response) throws Exception {
        EntityWrapper<Account> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("user_id", userId);
        }
        if (!Strings.isNullOrEmpty(userName)) {
            ew.like("username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(recAddr)) {
            ew.eq("rec_addr", recAddr);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.where("a.status = {0}", status);
        }
        if (!Strings.isNullOrEmpty(startAmount)) {
            ew.ge("balance_amount", startAmount);
        }
        if (!Strings.isNullOrEmpty(endAmount)) {
            ew.le("balance_amount", endAmount);
        }


        page.setCurrent(1);
        page.setSize(100000);//限制导出数量10W
        Page<Account> retPage = accountService.selectListPage(page, ew);
        String[] header = {"ID", "用户ID", "用户名", "真实姓名", "币种名称", "手机号码", "账户余额", "冻结金额", "钱包地址", "总充值", "总提现", "状态"};
        String[] properties = {"idStr", "userId", "userName", "realName", "coinName", "mobile", "balanceAmountStr", "freezeAmountStr", "recAddr", "rechargeAmountStr", "withdrawalsAmountStr", "statusStr"};

        CellProcessor[] PROCESSORS = new CellProcessor[]{
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                },
                null,
                null,
                null,
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
                null

        };
        String fileName = "账户资金.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, retPage.getRecords(), PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Log(value = "查询用户的钱包地址", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "query")

    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户的钱包地址", httpMethod = "GET")
    @RequestMapping("/selectUserWalletUrlList")
    public ResultMap selectUserWalletUrlList(int size, int current, String userId) {
        Page<Account> page = new Page<Account>(size, current);
        EntityWrapper<Account> ew = new EntityWrapper<>();
        ew.setSqlSelect("id,user_id,coin_id,user_name,real_name,coin_name,rec_addr,created");
        if (!Strings.isNullOrEmpty(userId)) {
            ew.eq("user_id", userId);
        }
        return ResultMap.getSuccessfulResult(accountService.selectListPage(page, ew));
    }

    /**
     * 虚拟币充值审
     *
     * @param accountDTO 请求参数
     * @param sysUser    当前登录用户
     * @return
     */
    @Log(value = "虚拟币充值", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('account_recharge_coin_xn')")
    @PostMapping("/rechargeCoin")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "虚拟币充值", httpMethod = "POST")
    @ApiImplicitParam(name = "accountDTO", value = "虚拟币充值参数", required = true, dataType = "AccountDTO", paramType = "body")
    @ResponseBody
    public ResultMap rechargeCoin(@RequestBody AccountDTO accountDTO, @ApiIgnore @AuthenticationPrincipal SysUser sysUser) {
        try {
            log.info("account rechargeCoin sysUser:" + sysUser);
            accountService.rechargeAmount(accountDTO, BusinessType.RECHARGE);
            return ResultMap.getSuccessfulResult("操作成功!");
        } catch (AccountException e) {
            return ResultMap.getFailureResult(1, e.getMessage());
        }
    }

    /**
     * 内部销户
     *
     *
     */

    @RequestMapping("/cancel")
    @ResponseBody
    public ResultMap accountCancel(@RequestParam Map<String, String> params) {
        try {
            String mobile = params.get("mobile");
            String email = params.get("email");
            String password = params.get("password");


            log.info("account cancel user:" );

            return ResultMap.getSuccessfulResult("操作成功!");
        } catch (Exception e) {
            return ResultMap.getFailureResult(1, e.getMessage());
        }
    }


}
