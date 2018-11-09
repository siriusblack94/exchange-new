package com.blockeng.admin.web.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.UserCashRechargeDTO;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CashRechargeService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
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
 * 充值表 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-13
 */
@Api(value = "法币充值", tags = {"法币充值"})
@RestController
@RequestMapping("/cashRecharge")
@Slf4j
public class CashRechargeController {

    @Autowired
    private CashRechargeService cashRechargeService;

    /**
     * @param current
     * @param size
     * @param userId
     * @param userName
     * @param mobile
     * @param status    充值状态：0，未付款；1，到账成功；2，人工到账；3，处理中
     *                  更改 0-待审核；1-审核通过；2-拒绝；3-充值成功
     * @param remarkId
     * @param numMin
     * @param numMax
     * @param startTime
     * @param endTime
     * @return
     */
    @Log(value = "查询CNY充值管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_audit_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY充值管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "'状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；", dataType = "String"),
            @ApiImplicitParam(name = "remarkId", value = "序列号", dataType = "String"),
            @ApiImplicitParam(name = "numMin", value = "数额-最小值", dataType = "String"),
            @ApiImplicitParam(name = "numMax", value = "数额-最大值", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserCashRechargeDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "userName", defaultValue = "") String userName,
                             @RequestParam(value = "mobile", defaultValue = "") String mobile,
                             @RequestParam(value = "status", defaultValue = "") String status,
                             @RequestParam(value = "remarkId", defaultValue = "") String remarkId,
                             @RequestParam(value = "numMin", defaultValue = "") String numMin,
                             @RequestParam(value = "numMax", defaultValue = "") String numMax,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<UserCashRechargeDTO> pager = new Page<>(current, size);
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("userId", userId);
        paramMap.put("userName", userName);
        paramMap.put("mobile", mobile);
        paramMap.put("status", status);
        paramMap.put("remarkId", remarkId);
        paramMap.put("numMin", numMin);
        paramMap.put("numMax", numMax);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        return ResultMap.getSuccessfulResult(cashRechargeService.selectMapPage(pager, paramMap));
    }

    /**
     * 导出人民币充值记录
     *
     * @param userName  用户名
     * @param remarkId  备注（序列号）
     * @param status    状态
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Log(value = "导出人民币充值记录", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('cash_recharge_audit_export')")
    @GetMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "导出人民币充值记录", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "'状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；", dataType = "String"),
            @ApiImplicitParam(name = "remarkId", value = "序列号", dataType = "String"),
            @ApiImplicitParam(name = "numMin", value = "数额-最小值", dataType = "String"),
            @ApiImplicitParam(name = "numMax", value = "数额-最大值", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @RequestMapping({"/exportCNYRecharge"})
    public void exportCNYRecharge(@RequestParam(value = "userId", defaultValue = "") String userId,
                                  @RequestParam(value = "userName", defaultValue = "") String userName,
                                  @RequestParam(value = "mobile", defaultValue = "") String mobile,
                                  @RequestParam(value = "status", defaultValue = "") String status,
                                  @RequestParam(value = "remarkId", defaultValue = "") String remarkId,
                                  @RequestParam(value = "numMin", defaultValue = "") String numMin,
                                  @RequestParam(value = "numMax", defaultValue = "") String numMax,
                                  @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                  @RequestParam(value = "endTime", defaultValue = "") String endTime, HttpServletResponse response) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<UserCashRechargeDTO> pager = new Page<>();
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        pager.setCurrent(0);
        pager.setTotal(0);
        pager.setSize(500000);

        paramMap.put("userId", userId);
        paramMap.put("userName", userName);
        paramMap.put("mobile", mobile);
        paramMap.put("status", status);
        paramMap.put("remarkId", remarkId);
        paramMap.put("numMin", numMin);
        paramMap.put("numMax", numMax);
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        String[] header = {"ID", "用户ID", "用户名", "真实用户名", "充值币种", "充值金额(USDT)", "手续费", "到账金额(CNY)", "充值方式", "充值订单", "参考号", "充值时间", "完成时间", "状态", "审核备注", "审核级数"};
        String[] properties = {"id", "userId", "username", "realName", "coinName", "num", "fee", "mum", "type", "tradeno", "remark", "created", "lastTime", "statusStr", "auditRemark", "step"};
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
                        String v = String.valueOf(value);
                        log.info("v---" + v);
                        if (v.equals("alipay")) {
                            return "支付宝";
                        }
                        if (v.equals("cai1pay")) {
                            return "财易付";
                        }
                        if (v.equals("bank")) {
                            return "银联";
                        }
                        if (v.equals("linepay")) {
                            return "在线充值";
                        }
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
                        String dateString = "";
                        if (value != null) {
                            dateString = "\t" + formatter.format(value);
                        }
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
        String fileName = "CNY充值记录.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, cashRechargeService.selectMapPage(pager, paramMap).getRecords(), PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取CNY充值单个信息
     *
     * @param id 充值id
     * @return
     */
    @Log(value = "获取CNY充值单个信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_audit_query')")
    @GetMapping
    @RequestMapping({"/getOneObj"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "获取CNY充值单个信息", httpMethod = "GET")
    @ApiImplicitParam(name = "id", value = "公告id", required = true, dataType = "Long")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserCashRechargeDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getOneObj(Long id) {
        log.info("CashRechargeController getOneObj id:" + id);
        if (null == id) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        UserCashRechargeDTO userCash = cashRechargeService.selectOneObj(id);
        return ResultMap.getSuccessfulResult(userCash);
    }

    /**
     * 法币充值审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    @Log(value = "法币充值审核", type = SysLogTypeEnum.AUDIT)
    @PostMapping("/cashRechargeUpdateStatus")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "法币充值审核", httpMethod = "POST")
    @ApiImplicitParam(name = "auditDTO", value = "法币充值审核请求参数", required = true, dataType = "AuditDTO", paramType = "body")
    @ResponseBody
    public ResultMap cashRechargeAudit(@RequestBody AuditDTO auditDTO, @ApiIgnore @AuthenticationPrincipal SysUser sysUser) {
        try {
            cashRechargeService.cashRechargeAudit(auditDTO, sysUser);
            return ResultMap.getSuccessfulResult("操作成功!");
        } catch (ExchangeException e) {
            return ResultMap.getFailureResult(1, e.getMessage());
        }
    }
}
