package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CoinWithdrawalsCountDTO;
import com.blockeng.admin.dto.DigitalCoinWithdrawStatisticsDTO;
import com.blockeng.admin.entity.DigitalCoinWithdrawStatistics;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinWithdrawService;
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
 * @Dec 提币统计
 **/
@Slf4j
@Api(value = "提币统计controller", tags = {"提币统计"})
@RestController
@RequestMapping("/coinWithdrawalsCount")
public class CoinWithdrawalsCountController {

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Log(value = "提币统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('coin_withdraw_statistics_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "提币统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             String coinName) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<CoinWithdrawalsCountDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);

        Page<CoinWithdrawalsCountDTO> pt = coinWithdrawService.selectCountMain(pager, paramMap);
        if (pt != null && pt.getRecords().size() > 0) {
            String dates[] = new String[pt.getRecords().size()];
            int i = 0;
            for (CoinWithdrawalsCountDTO c : pt.getRecords()) {
                dates[i] = c.getCreated();
                i++;
            }
            paramMap.put("createds", dates);
            List<CoinWithdrawalsCountDTO> pt2 = coinWithdrawService.selectValidCounts(paramMap);
            List<CoinWithdrawalsCountDTO> pt3 = coinWithdrawService.selectUserCt(paramMap);
            String validCounts = "0";
            for (CoinWithdrawalsCountDTO c : pt.getRecords()) {
                for (CoinWithdrawalsCountDTO c2 : pt2) {
                    if (c.getCreated().equals(c2.getCreated())) {
                        c.setValidCounts(c2.getValidCounts());
                    }
                }
                if (c.getValidCounts() == null) {
                    c.setValidCounts(validCounts);
                }
                int oldCount = 0;
                for (CoinWithdrawalsCountDTO c3 : pt3) {
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


    @Log(value = "提币统计", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/digitalCoinWithdrawStatistics"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "提币统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "userId", defaultValue = "") String userId
                             ) {

        DigitalCoinWithdrawStatisticsDTO digitalCoinWithdrawStatisticsDTO=coinWithdrawService.selectDigitalCoinWithdrawStatistics(current,size,startTime,endTime,coinId,userId);
        return ResultMap.getSuccessfulResult(digitalCoinWithdrawStatisticsDTO);
    }


    @Log(value = "提币统计导出", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/digitalCoinWithdrawStatistics/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "提币统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinWithdrawalsCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public void exportList(
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             HttpServletResponse response
    ) {

        DigitalCoinWithdrawStatisticsDTO digitalCoinWithdrawStatisticsDTO=coinWithdrawService.selectDigitalCoinWithdrawStatistics(1,10000000,startTime,endTime,coinId,userId);
        List list=digitalCoinWithdrawStatisticsDTO.getRecords();

        DigitalCoinWithdrawStatistics digitalCoinWithdrawStatistics=new DigitalCoinWithdrawStatistics();
        digitalCoinWithdrawStatistics.setUserId("合计");
        digitalCoinWithdrawStatistics.setCoinName(digitalCoinWithdrawStatisticsDTO.getCoinName());
        digitalCoinWithdrawStatistics.setWithdrawCount(digitalCoinWithdrawStatisticsDTO.getWithdrawCount());
        digitalCoinWithdrawStatistics.setFee(digitalCoinWithdrawStatisticsDTO.getFee());
        digitalCoinWithdrawStatistics.setRealWithdrawCount(digitalCoinWithdrawStatisticsDTO.getRealWithdrawCount());
        digitalCoinWithdrawStatistics.setWithdrawTimes(digitalCoinWithdrawStatisticsDTO.getWithdrawTimes());

        list.add(0,digitalCoinWithdrawStatistics);

        String[] header = {"用户ID","币种","提币数量","手续费","实际提币数量","提现次数"};
        String[] properties = {"userId","coinName","withdrawCount","fee","realWithdrawCount","withdrawTimes"};
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
                },
                new CellProcessorAdaptor() {
                    @Override
                    public String execute(Object value, CsvContext context) {
                        String v = "\t" + String.valueOf(value);
                        return v;
                    }
                }


        };
        String fileName = "数字币提币统计.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

