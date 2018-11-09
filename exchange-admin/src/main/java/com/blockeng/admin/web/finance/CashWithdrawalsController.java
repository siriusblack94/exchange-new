package com.blockeng.admin.web.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.UserWithDrawalsDTO;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.*;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.framework.exception.ExchangeException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 提现表 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
@RestController
@RequestMapping("/cashWithdrawals")
@Slf4j
@Api(value = "CNY提现", tags = {"CNY提现"})
public class CashWithdrawalsController {

    @Autowired
    private CashWithdrawalsService cashWithdrawalsService;

    /**
     * @param current
     * @param size
     * @param userId
     * @param userName
     * @param mobile
     * @param status    0-待审核；1-审核通过；2-拒绝；3-提现成功
     * @param coinId
     * @param numMin
     * @param numMax
     * @param startTime
     * @param endTime
     * @return
     */
    @Log(value = "CNY提现管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_audit_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY提现管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "'充值状态：0-待审核；1-审核通过；2-拒绝；3-提现成功", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种id", dataType = "String"),
            @ApiImplicitParam(name = "numMin", value = "数额-最小值", dataType = "String"),
            @ApiImplicitParam(name = "numMax", value = "数额-最大值", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserWithDrawalsDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(int current,
                             int size,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "userName", defaultValue = "") String userName,
                             @RequestParam(value = "mobile", defaultValue = "") String mobile,
                             @RequestParam(value = "status", defaultValue = "") String status,
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "numMin", defaultValue = "") String numMin,
                             @RequestParam(value = "numMax", defaultValue = "") String numMax,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<UserWithDrawalsDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("userId", userId);
        paramMap.put("userName", userName);
        paramMap.put("mobile", mobile);
        paramMap.put("status", status);
        paramMap.put("coinId", coinId);
        paramMap.put("numMin", numMin);
        paramMap.put("numMax", numMax);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return ResultMap.getSuccessfulResult(cashWithdrawalsService.selectMapPage(pager, paramMap));
    }

    /**
     * 获取CNY提现单个信息
     *
     * @param id 提现id
     * @return
     */
    @Log(value = "获取CNY提现单个信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_audit_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取CNY提现单个信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "提现id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserWithDrawalsDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("CashWithdrawalsController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        UserWithDrawalsDTO userWithCash = cashWithdrawalsService.selectOneObj(id);
        return ResultMap.getSuccessfulResult(userWithCash);
    }

    @Log(value = "导出人民币提现记录", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('cash_withdraw_audit_export')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "导出人民币提现记录", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "充值状态", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "trueName", value = "真实名", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @RequestMapping({"/exportCNYWithDrawals"})
    public void exportCNYWithDrawals(@RequestParam(value = "userId", defaultValue = "") String userId,
                                     @RequestParam(value = "userName", defaultValue = "") String userName,
                                     @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                     @RequestParam(value = "status", defaultValue = "") String status,
                                     @RequestParam(value = "coinId", defaultValue = "") String coinId,
                                     @RequestParam(value = "numMin", defaultValue = "") String numMin,
                                     @RequestParam(value = "numMax", defaultValue = "") String numMax,
                                     @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                     @RequestParam(value = "endTime", defaultValue = "") String endTime, HttpServletResponse response) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<UserWithDrawalsDTO> pager = new Page<>(1, 500000);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("userId", userId);
        paramMap.put("userName", userName);
        paramMap.put("mobile", mobile);
        paramMap.put("status", status);
        paramMap.put("coinId", coinId);
        paramMap.put("numMin", numMin);
        paramMap.put("numMax", numMax);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        String[] header = {"ID", "用户ID", "用户名", "提现金额(USDT)", "手续费", "到账金额", "提现开户名", "银行名称/账号", "申请时间", "完成时间", "状态", "备注", "审核级数"};
        String[] properties = {"id", "userId", "username", "num", "fee", "realAmount", "truename", "bankNameAndCard", "created", "lastTime", "statusStr", "remark", "step"};
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //有多少列就写多少个，不要处理的就null
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
                null,
                null,
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String dateString = "\t" + formatter.format(value);
                        return dateString;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String dateString = "";
                        if (value != null) {
                            dateString = "\t" + formatter.format(value);
                        }
                        return dateString;
                    }
                },
                null, null, null};
        String fileName = "CNY提现记录.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, cashWithdrawalsService.selectMapPage(pager, paramMap).getRecords(), PROCESSORS);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 法币提现审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    @Log(value = "法币提现审核", type = SysLogTypeEnum.AUDIT)
    @RequestMapping(value = {"/updateWithdrawalsStatus"}, method = RequestMethod.POST)
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "法币提现审核", httpMethod = "POST")
    @ApiImplicitParam(name = "auditDTO", value = "法币提现审核请求参数", required = true, dataType = "AuditDTO", paramType = "body")
    @ResponseBody
    public ResultMap cashWithdrawAudit(@RequestBody AuditDTO auditDTO, @ApiIgnore @AuthenticationPrincipal SysUser sysUser) {
        try {
            cashWithdrawalsService.cashWithdrawAudit(auditDTO, sysUser);
            return ResultMap.getSuccessfulResult("操作成功!");
        } catch (ExchangeException e) {
            return ResultMap.getFailureResult(1, e.getMessage());
        }
    }
}
