package com.blockeng.admin.web.finance;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.CoinWithDrawDTO;
import com.blockeng.admin.dto.CoinWithdrawRetryRecordDTO;
import com.blockeng.admin.entity.CoinWithdraw;
import com.blockeng.admin.entity.CoinWithdrawRetryRecord;
import com.blockeng.admin.entity.SysUser;
import com.blockeng.admin.entity.TurnoverOrder;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinWithdrawRetryRecordService;
import com.blockeng.admin.service.CoinWithdrawService;
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

/**
 * <p>
 * 虚拟币提现 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-17
 */
@Slf4j
@RestController
@RequestMapping("/coinWithdraw")
@Api(value = "虚拟币提现", description = "虚拟币提现管理")
public class CoinWithdrawController {

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Autowired
    private CoinWithdrawRetryRecordService coinWithdrawRetryRecordService;

    @Log(value = "查询提现列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('coin_withdraw_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startAmount", value = "开始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endAmount", value = "结束金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询提现列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<CoinWithdraw> page,
                             String userName, String mobile,
                             String coinId, String status,
                             String startAmount, String endAmount,
                             String startTime, String endTime) {
        EntityWrapper<CoinWithdraw> ew = new EntityWrapper<>();

        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        if (!Strings.isNullOrEmpty(userName)) {
            ew.like("b.username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(startAmount)) {
            ew.ge("a.mum", startAmount);
        }
        if (!Strings.isNullOrEmpty(endAmount)) {
            ew.le("a.mum", endAmount);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("a.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ew.le("a.created", endTime);
        }
        Page<CoinWithdraw> coinWithdrawPage = coinWithdrawService.selectListPage(page, ew);
        return ResultMap.getSuccessfulResult(coinWithdrawPage);
    }

    @Log(value = "提现记录导出", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('coin_withdraw_export')")
    @RequestMapping({"/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "提现记录导出", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态(0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中)", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startAmount", value = "起始金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endAmount", value = "结束金额", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String", paramType = "query"),
    })
    public void export(@ApiIgnore Page<CoinWithdraw> page,
                       String userName, String mobile,
                       String coinId, String status,
                       String startAmount, String endAmount,
                       String startTime, String endTime
            , HttpServletResponse response) {
        EntityWrapper<CoinWithdraw> ew = new EntityWrapper<>();

        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userName)) {
            ew.like("b.username", userName);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("a.status", status);
        }
        if (!Strings.isNullOrEmpty(startAmount)) {
            ew.ge("a.mum", startAmount);
        }
        if (!Strings.isNullOrEmpty(endAmount)) {
            ew.le("a.mum", endAmount);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("a.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ew.le("a.created", endTime);
        }


        page.setCurrent(1);
        page.setSize(500000);//限制条数
        Page<CoinWithdraw> retPage = coinWithdrawService.selectListPage(page, ew);

        String[] header = {"订单ID", "用户名", "币种名称", "提现量", "实际提现", "手续费", "钱包地址", "交易ID", "申请时间", "审核时间", "审核备注", "状态", "审核级数"};
        String[] properties = {"id", "userName", "coinName", "num", "mum", "fee", "address", "txid", "created", "auditTime", "remark", "statusStr", "step"};
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        CellProcessor[] PROCESSORS = new CellProcessor[]{
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
                        log.info("--num---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--num1---" + v);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        log.info("--mum---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--mum1---" + v);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        log.info("--fee---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {

                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--fee1---" + v);
                        }
                        return "\t" + v;
                    }
                },
                null,
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
                }, null, null, null};

        String fileName = "提币记录.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, retPage.getRecords(), PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提币审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    @Log(value = "提币审核", type = SysLogTypeEnum.AUDIT)
    @PostMapping("/audit")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "提现审核(status:2不通过 3通过)", httpMethod = "POST")
    @ApiImplicitParam(name = "auditDTO", value = "提币审核请求参数", required = true, dataType = "AuditDTO", paramType = "body")
    @ResponseBody
    public Object coinWithdrawAudit(@RequestBody AuditDTO auditDTO, @ApiIgnore @AuthenticationPrincipal SysUser sysUser) {
        try {
            coinWithdrawService.coinWithdrawAudit(auditDTO, sysUser);
            return ResultMap.getSuccessfulResult("操作成功!");
        } catch (ExchangeException e) {
            return ResultMap.getFailureResult(e.getMessage());
        }
    }

    /**
     * 手工打币成功
     *
     * @param coinWithDrawDTO 提币实体
     * @return
     */
    @Log(value = "手工提币成功", type = SysLogTypeEnum.UPDATE)
//    @PreAuthorize("hasAuthority('no_automatic_pay')")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "手工提现", httpMethod = "PUT")
    @ApiImplicitParam(name = "coinWithDrawDTO", value = "提币审核请求参数", required = true, dataType = "CoinWithDrawDTO", paramType = "body")
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public Object withDrawSuccess(@RequestBody CoinWithDrawDTO coinWithDrawDTO) {
        if (coinWithDrawDTO.getId() > 0 && StringUtils.isEmpty(coinWithDrawDTO.getTxid())) {
            return coinWithdrawService.withDrawSuccess(coinWithDrawDTO);
        }
        return coinWithdrawService.withDrawSuccess(coinWithDrawDTO);
    }

    /**
     * 重新打币
     *
     * @param
     */
    @PostMapping(value = "/retry")
    public Object reTryWithdraw(@RequestBody AuditDTO retryDTO) {
        try {

            if (coinWithdrawService.reTryWithdraw(retryDTO) == 1) {
                return ResultMap.getSuccessfulResult("操作成功!");
            }
            return ResultMap.getSuccessfulResult("操作失败!");
        } catch (ExchangeException e) {
            return ResultMap.getFailureResult(e.getMessage());
        }
    }

    /**
     * 查询重新打币记录
     */
    @GetMapping("/retryRecord")
    public Object reTryWithdrawRecord(Page<CoinWithdrawRetryRecordDTO> page,
                                      String userId, String mobile,
                                      String coinId, String mum,
                                      String startTime, String endTime) {
        EntityWrapper<CoinWithdrawRetryRecord> ew = new EntityWrapper<>();
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.like("b.id", userId);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(mum)) {
            ew.eq("a.mum", mum);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("a.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ew.le("a.created", endTime);
        }
        ew.eq("d.step", 1);
        Page<CoinWithdrawRetryRecordDTO> coinWithdrawRetryRecordDTOPage = coinWithdrawRetryRecordService.selectListPage(page, ew);
        return ResultMap.getSuccessfulResult(coinWithdrawRetryRecordDTOPage);
    }

    @Log(value = "重新打币记录导出", type = SysLogTypeEnum.EXPORT)
    @GetMapping({"/exportRetryRecord"})
    @ResponseBody
    public void exportRetryRecord(Page<CoinWithdrawRetryRecordDTO> page,
                                  String userId, String mobile,
                                  String coinId, String mum,
                                  String startTime, String endTime,
                                  HttpServletResponse response) {
        EntityWrapper<CoinWithdrawRetryRecord> ew = new EntityWrapper<>();
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        if (!Strings.isNullOrEmpty(userId)) {
            ew.like("b.id", userId);
        }
        if (!Strings.isNullOrEmpty(mobile)) {
            ew.like("b.mobile", mobile);
        }
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("a.coin_id", coinId);
        }
        if (!Strings.isNullOrEmpty(mum)) {
            ew.eq("a.mum", mum);
        }
        if (!Strings.isNullOrEmpty(startTime)) {
            ew.ge("a.created", startTime);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ew.le("a.created", endTime);
        }

        page.setCurrent(1);
        page.setSize(100000);//限制条数
        Page<CoinWithdrawRetryRecordDTO> retPage = coinWithdrawRetryRecordService.selectListPage(page, ew);

        String[] header = {"订单ID", "用户ID", "用户名", "币种名称", "提现量", "实际提现", "手续费", "钱包地址", "交易ID", "审核备注", "一级审核人员", "二级审核人员"};
        String[] properties = {"id", "userId", "userName", "coinName", "num", "mum", "fee", "address", "txid", "remark", "auditUserName", "secondAuditUserName"};

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        log.info("--num---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--num1---" + v);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        log.info("--mum---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {
                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--mum1---" + v);

                        }
                        return "\t" + v;
                    }
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        log.info("--fee---" + value);
                        String v = String.valueOf(value);
                        if (value != null) {

                            DecimalFormat df = new DecimalFormat("0.00000000");
                            v = df.format(value);
                            log.info("--fee1---" + v);
                        }
                        return "\t" + v;
                    }
                }, null, null, null, null, null};

        String fileName = "重现打币记录.csv";
        try {


            ReportCsvUtils.reportListCsv(response, header, properties, fileName, retPage.getRecords(), PROCESSORS);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}



