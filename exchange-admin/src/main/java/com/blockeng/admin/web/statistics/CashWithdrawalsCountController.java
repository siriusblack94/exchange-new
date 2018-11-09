package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CashWithdrawalsCountDTO;
import com.blockeng.admin.dto.CurbExchangeWithdrawStatisticsDTO;
import com.blockeng.admin.entity.CurbExchangeWithdrawStatistics;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CashWithdrawalsService;
import com.blockeng.admin.view.ReportCsvUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create Time: 2018年06月01日 10:16
 *
 * @author lxl
 * @Dec CNY提现统计
 **/
@Slf4j
@Api(value = "CNY提现统计controller", tags = {"CNY提现统计"})
@RestController
@RequestMapping("/cashWithdrawalsCount")
public class CashWithdrawalsCountController {

    @Autowired
    private CashWithdrawalsService cashWithdrawalsService;

    @Log(value = "CNY提现统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_statistics_query')")
    @GetMapping
    @RequestMapping({"/curbExchangeWithdrawStatistics"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY提现统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CashWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap curbExchangeWithdrawStatistics(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                                    @RequestParam(value = "userId", defaultValue = "") String userId
                             ) {

        CurbExchangeWithdrawStatisticsDTO curbExchangeWithdrawStatisticsDTO=cashWithdrawalsService.selectCurbExchangeWithdrawStatistics(current,size,startTime,endTime,userId);
        return ResultMap.getSuccessfulResult(curbExchangeWithdrawStatisticsDTO);
    }


    @Log(value = "CNY提现统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_statistics_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY提现统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CashWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             String coinName) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<CashWithdrawalsCountDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        Page<CashWithdrawalsCountDTO> pt = cashWithdrawalsService.selectCountMain(pager, paramMap);
        if (pt != null && pt.getRecords().size() > 0) {
            String dates[] = new String[pt.getRecords().size()];
            int i = 0;
            for (CashWithdrawalsCountDTO c : pt.getRecords()) {
                dates[i] = c.getCreated();
                i++;
            }
            paramMap.put("createds", dates);
            List<CashWithdrawalsCountDTO> pt2 = cashWithdrawalsService.selectValidCounts(paramMap);
            List<CashWithdrawalsCountDTO> pt3 = cashWithdrawalsService.selectUserCt(paramMap);
            String validCounts = "0";
            for (CashWithdrawalsCountDTO c : pt.getRecords()) {
                for (CashWithdrawalsCountDTO c2 : pt2) {
                    if (c.getCreated().equals(c2.getCreated())) {
                        c.setValidCounts(c2.getValidCounts());
                    }
                }
                if (c.getValidCounts() == null) {
                    c.setValidCounts(validCounts);
                }
                int oldCount = 0;
                for (CashWithdrawalsCountDTO c3 : pt3) {
                    if (c.getCreated().equals(c3.getCreated())) {
                        if (Integer.valueOf(c3.getUserCt()) > oldCount) {
                            c.setUserId(c3.getUserId());
                            c.setUserCt(c3.getUserCt());
                            oldCount = Integer.valueOf(c3.getUserCt());
                        }
                    }
                }
            }
        }
        return ResultMap.getSuccessfulResult(pt);
    }

    @Log(value = "场外交易统计导出", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_statistics_query')")
    @GetMapping
    @RequestMapping({"/curbExchangeWithdrawStatistics/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY提现统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CashWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public void curbExchangeWithdrawStatistics(
                                                    @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                                    @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                                    @RequestParam(value = "userId", defaultValue = "") String userId,HttpServletResponse response
    ) {

        CurbExchangeWithdrawStatisticsDTO curbExchangeWithdrawStatisticsDTO=cashWithdrawalsService.selectCurbExchangeWithdrawStatistics(1,10000000,startTime,endTime,userId);
        List list=curbExchangeWithdrawStatisticsDTO.getRecords();

        CurbExchangeWithdrawStatistics curbExchangeRechargeStatistics=new CurbExchangeWithdrawStatistics();
        curbExchangeRechargeStatistics.setUserId("合计");
        curbExchangeRechargeStatistics.setWithdrawAmount(curbExchangeWithdrawStatisticsDTO.getWithdrawAmount());
        curbExchangeRechargeStatistics.setFee(curbExchangeWithdrawStatisticsDTO.getFee());
        curbExchangeRechargeStatistics.setTransferAmount(curbExchangeWithdrawStatisticsDTO.getTransferAmount());
        curbExchangeRechargeStatistics.setWithdrawTimes(curbExchangeWithdrawStatisticsDTO.getWithdrawTimes());

        list.add(0,curbExchangeRechargeStatistics);

        String[] header = {"用户ID","提现金额","手续费","到帐金额","充值次数"};
        String[] properties = {"userId","withdrawAmount","fee","transferAmount","withdrawTimes"};
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
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                }

        };
        String fileName = "场外交易提现统计导出.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
