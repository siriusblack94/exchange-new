package com.blockeng.admin.web.finance;

import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CoinRechargeDTO;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinRechargeService;
import com.blockeng.admin.view.ReportCsvUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-18
 */
@RestController
@RequestMapping("/coinRecharge")
@Api(value = "虚拟币充值记录", description = "虚拟币充值记录")
public class CoinRechargeController {

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Log(value = "查询虚拟币充值管理列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('coin_recharge_query')")
    @GetMapping
    @RequestMapping({"/userInWalletList"})
    @ResponseBody
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "虚拟币充值管理列表", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "'状态", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种id", dataType = "String"),
            @ApiImplicitParam(name = "numMin", value = "数额-最小值", dataType = "String"),
            @ApiImplicitParam(name = "numMax", value = "数额-最大值", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    public ResultMap userInWalletList(int current,
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
        Page<CoinRechargeDTO> pager = new Page<>(current, size);
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
        return ResultMap.getSuccessfulResult(coinRechargeService.selectMapPage(pager, paramMap));
    }

    @Log(value = "导出数字货币充值记录", type = SysLogTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('coin_recharge_export')")
    @GetMapping
    @RequestMapping({"/exportCoinRecharge"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "导出数字货币充值记录", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String"),
            @ApiImplicitParam(name = "userName", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataType = "String"),
            @ApiImplicitParam(name = "status", value = "'状态", dataType = "String"),
            @ApiImplicitParam(name = "coinId", value = "币种id", dataType = "String"),
            @ApiImplicitParam(name = "numMin", value = "数额-最小值", dataType = "String"),
            @ApiImplicitParam(name = "numMax", value = "数额-最大值", dataType = "String"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "String")
    })
    public void exportCoinRecharge(
            HttpServletResponse response,
            String userId,
            String userName,
            String mobile,
            String status,
            String coinId,
            String numMin,
            String numMax,
            String startTime,
            String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        Page<CoinRechargeDTO> pager = new Page<>(0, 500000);
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
        Page<CoinRechargeDTO> pager1 = coinRechargeService.selectMapPage(pager, paramMap);
        List<CoinRechargeDTO> dataList = pager1.getRecords();
        String[] header = {"ID", "用户ID", "用户名", "币种名称", "充值数量", "收款地址", "交易ID", "充值时间", "状态"};
        //   String[] properties = {"id", "userId", "username", "coinName", "num", "address", "txid", "created", "statusStr"}; 修改
        String[] properties = {"id", "userId", "username", "coinName", "amount", "address", "txid", "created", "statusStr"};

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
                        String dateString = "";
                        if (value != null) {
                            dateString = "\t" + formatter.format(value);
                        }
                        return dateString;
                    }
                },
                null};
        String fileName = "数字货币充值记录.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, dataList, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
