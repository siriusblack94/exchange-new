package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CashRechargeCountDTO;
import com.blockeng.admin.dto.CurbExchangeRechargeStatisticsDTO;
import com.blockeng.admin.entity.CurbExchangeRechargeStatistics;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CashRechargeService;
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
 * CNY充值统计
 *
 * @author lxl
 * @date 2018-05-31
 */

@Slf4j
@Api(value = "CNY充值统计controller", tags = {"CNY充值统计"})
@RestController
@RequestMapping("/cashRechargeCount")
public class CashRechargeCountController {

    @Autowired
    private CashRechargeService cashRechargeService;

    @Log(value = "CNY充值统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_statistics_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY充值统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CashRechargeCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             String coinName) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<CashRechargeCountDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        Page<CashRechargeCountDTO> pt = cashRechargeService.selectCountMain(pager, paramMap);
        if (pt != null && pt.getRecords().size() > 0) {
            String dates[] = new String[pt.getRecords().size()];
            int i = 0;
            for (CashRechargeCountDTO c : pt.getRecords()) {
                dates[i] = c.getCreated();
                i++;
            }
            paramMap.put("createds", dates);
            List<CashRechargeCountDTO> pt2 = cashRechargeService.selectValidCounts(paramMap);
            List<CashRechargeCountDTO> pt3 = cashRechargeService.selectUserCt(paramMap);
            String validCounts = "0";
            for (CashRechargeCountDTO c : pt.getRecords()) {
                for (CashRechargeCountDTO c2 : pt2) {
                    if (c.getCreated().equals(c2.getCreated())) {
                        c.setValidCounts(c2.getValidCounts());
                    }
                }
                if (c.getValidCounts() == null) {
                    c.setValidCounts(validCounts);
                }
                int oldCount = 0;
                for (CashRechargeCountDTO c3 : pt3) {
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


    @Log(value = "CNY充值统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_statistics_query')")
    @GetMapping
    @RequestMapping({"/curbExchangeRechargeStatistics"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY充值统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })

    public ResultMap curbExchangeRechargeStatistics(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             @RequestParam(value = "userId",defaultValue = "")String userId
                             ) {
        CurbExchangeRechargeStatisticsDTO curbExchangeRechargeStatisticsDTO=cashRechargeService.selectCurbExchangeRechargeStatistics(current,size,startTime,endTime,userId);
        List list=curbExchangeRechargeStatisticsDTO.getRecords();


        return ResultMap.getSuccessfulResult(curbExchangeRechargeStatisticsDTO);
    }


    @Log(value = "场外交易充值统计导出", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_statistics_query')")
    @GetMapping
    @RequestMapping({"/curbExchangeRechargeStatistics/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "CNY充值统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })

    public void curbExchangeRechargeStatisticsExportList(
                                                    @RequestParam(value = "startTime", defaultValue = "") String startTime,
                                                    @RequestParam(value = "endTime", defaultValue = "") String endTime,
                                                    @RequestParam(value = "userId",defaultValue = "")String userId,HttpServletResponse response
    ) {
        CurbExchangeRechargeStatisticsDTO curbExchangeRechargeStatisticsDTO=cashRechargeService.selectCurbExchangeRechargeStatistics(1,10000000,startTime,endTime,userId);
        List list=curbExchangeRechargeStatisticsDTO.getRecords();

        CurbExchangeRechargeStatistics curbExchangeRechargeStatistics=new CurbExchangeRechargeStatistics();
        curbExchangeRechargeStatistics.setUserId("合计");
        curbExchangeRechargeStatistics.setRechargeAmount(curbExchangeRechargeStatisticsDTO.getRechageAmount());
        curbExchangeRechargeStatistics.setTransferAmount(curbExchangeRechargeStatisticsDTO.getTransferAmount());
        curbExchangeRechargeStatistics.setRechargeTimes(curbExchangeRechargeStatisticsDTO.getRechargeTimes());

        list.add(0,curbExchangeRechargeStatistics);

        String[] header = {"用户ID","充值金额","到帐金额","充值次数"};
        String[] properties = {"userId","rechargeAmount","transferAmount","rechargeTimes"};
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
                }



        };
        String fileName = "场外交易充值统计导出.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
