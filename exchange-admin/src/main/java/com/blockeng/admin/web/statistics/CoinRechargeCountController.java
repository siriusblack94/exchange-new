package com.blockeng.admin.web.statistics;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CoinRechargeCountDTO;
import com.blockeng.admin.dto.DigitalCoinRechargeStatisticsDTO;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.DigitalCoinRechargeStatistics;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountDetailService;
import com.blockeng.admin.service.CoinRechargeService;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.view.ReportCsvUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.HttpResource;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充币统计
 *
 * @author lxl
 * @date 2018-05-31
 */

@Slf4j
@Api(value = "充币统计controller", tags = {"充币统计"})
@RestController
@RequestMapping("/coinRechargeCount")
public class CoinRechargeCountController {

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private AccountDetailService accountDetailService;


    @Log(value = "充币统计", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('coin_recharge_statistics_query')")
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "充币统计列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinRechargeCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<CoinRechargeCountDTO> pager = new Page<>(current, size);
        if (StringUtils.isNotBlank(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        Page<CoinRechargeCountDTO> pt = coinRechargeService.selectCountMain(pager, paramMap);
        if (pt != null && pt.getRecords().size() > 0) {
            String dates[] = new String[pt.getRecords().size()];
            int i = 0;
            for (CoinRechargeCountDTO c : pt.getRecords()) {
                dates[i] = c.getCreated();
                i++;
            }
            paramMap.put("createds", dates);
            List<CoinRechargeCountDTO> pt3 = coinRechargeService.selectUserCt(paramMap);
            for (CoinRechargeCountDTO c : pt.getRecords()) {
                int oldCount = 0;
                for (CoinRechargeCountDTO c3 : pt3) {
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




    @Log(value = "充币统计", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/digitalCoinRechargeStatistics"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "数字币充币统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种id", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "rechargeMethod", value = "充值方式", dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinRechargeCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(@RequestParam(value = "current", defaultValue = "1") int current,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "startTime", defaultValue = "") String startTime,
                             @RequestParam(value = "endTime", defaultValue = "") String endTime,
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "rechargeMethod", defaultValue = "0") int rechargeMethod) {

        DigitalCoinRechargeStatisticsDTO digitalCoinRechargeStatisticsDTO=accountDetailService.selectDigitalCoinRechargeStatistics(current,size,startTime,endTime,coinId,userId,rechargeMethod);

        return ResultMap.getSuccessfulResult(digitalCoinRechargeStatisticsDTO);

    }


    @Log(value = "充币统计导出", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/digitalCoinRechargeStatistics/exportList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "数字币充币统计", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种id", dataType = "String"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "rechargeMethod", value = "充值方式", dataType = "int")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinRechargeCountDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public void exportList(
                           @RequestParam(value = "startTime", defaultValue = "") String startTime,
                           @RequestParam(value = "endTime", defaultValue = "") String endTime,
                           @RequestParam(value = "coinId", defaultValue = "") String coinId,
                           @RequestParam(value = "userId", defaultValue = "") String userId,
                           @RequestParam(value = "rechargeMethod", defaultValue = "0") int rechargeMethod,
                           HttpServletResponse response) {

        DigitalCoinRechargeStatisticsDTO digitalCoinRechargeStatisticsDTO=accountDetailService.selectDigitalCoinRechargeStatistics(1,10000000,startTime,endTime,coinId,userId,rechargeMethod);
        List list=digitalCoinRechargeStatisticsDTO.getRecords();

        DigitalCoinRechargeStatistics digitalCoinRechargeStatistics=new DigitalCoinRechargeStatistics();
        digitalCoinRechargeStatistics.setUserId("合计");
        digitalCoinRechargeStatistics.setCoinName(digitalCoinRechargeStatisticsDTO.getCoinName());
        digitalCoinRechargeStatistics.setRechargeCount(digitalCoinRechargeStatisticsDTO.getRechargeCount());
        digitalCoinRechargeStatistics.setRechargeMethod(digitalCoinRechargeStatisticsDTO.getRechargeMethod());
        digitalCoinRechargeStatistics.setRechargeTimes(digitalCoinRechargeStatisticsDTO.getRechargeTimes());
        list.add(0,digitalCoinRechargeStatistics);

        String[] header = {"用户ID","币种","充值数量","方式","充值次数"};
        String[] properties = {"userId","coinName","rechargeCount","rechargeMethod","rechargeTimes"};
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
        String fileName = "数字币充值统计.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, list, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

