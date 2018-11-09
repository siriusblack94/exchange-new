package com.blockeng.admin.web.statistics;


import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.AccountBalanceStatiscDTO;
import com.blockeng.admin.entity.AccountBalance;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountBalanceCountService;
import com.blockeng.admin.view.ReportCsvUtils;
import com.blockeng.framework.http.Response;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.util.CsvContext;

import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.List;


/**
 * 账户余额统计
 * */

@Slf4j
@Api(value = "账户余额统计Controller", tags = {"账户余额统计"})
@RestController
@RequestMapping("/accountBalanceCount")
public class AccountBalanceCountController {


    @Autowired
    private AccountBalanceCountService accountBalanceCountService;

    @Log(value = "账户余额统计", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/getList"})
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "钱包类型", dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "用户类型", dataType = "int"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = AccountBalanceStatiscDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap getList(
                             @RequestParam(value = "coinId", defaultValue = "") String coinId,
                             @RequestParam(value = "userType", defaultValue = "") int userType
                             ) {
        AccountBalanceStatiscDTO accountBalanceStatiscDTO=accountBalanceCountService.accountBalance(coinId,userType);

        return ResultMap.getSuccessfulResult(accountBalanceStatiscDTO);
    }



    @Log(value = "账户余额统计报表导出", type = SysLogTypeEnum.SELECT)
    @GetMapping
    @RequestMapping({"/exportList"})
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coinId", value = "钱包类型", dataType = "String"),
            @ApiImplicitParam(name = "userType", value = "用户类型", dataType = "int"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = AccountBalanceStatiscDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public void exportList(
            @RequestParam(value = "coinId", defaultValue = "") String coinId,
            @RequestParam(value = "userType", defaultValue = "") int userType,
            HttpServletResponse response
    ) {



        AccountBalanceStatiscDTO accountBalanceStatiscDTO=accountBalanceCountService.accountBalance(coinId,userType);
        List<AccountBalance> accountBalanceList=accountBalanceStatiscDTO.getRecords();
        AccountBalance accountBalance=new AccountBalance();
        accountBalance.setUserType("合计");
        accountBalance.setUserCount(null);
        accountBalance.setWalletType(accountBalanceStatiscDTO.getWalletType());
        accountBalance.setTotalBalance(accountBalanceStatiscDTO.getTotalBalance());
        accountBalance.setAvailableBalance(accountBalanceStatiscDTO.getAvailableBalance());
        accountBalance.setTransactionFreeze(accountBalanceStatiscDTO.getTransactionFreeze());
        accountBalance.setWithdrawFreeze(accountBalanceStatiscDTO.getWithdrawFreeze());
        accountBalance.setBuckleFreeze(accountBalanceStatiscDTO.getBuckleFreeze());
        accountBalanceList.add(0,accountBalance);

        String[] header = {"用户类型","用户数量","钱包类型","总资产","可用余额","交易冻结","提现冻结","补扣冻结"};
        String[] properties = {"userType", "userCount", "walletType", "totalBalance", "availableBalance", "transactionFreeze", "withdrawFreeze","buckleFreeze"};
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

                        String v;
                        if(value==null){
                            v= "\t";
                        }else{
                            v= "\t"+String.valueOf(value);
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
        String fileName = "账户余额统计.csv";
        try {
            ReportCsvUtils.reportListCsv(response, header, properties, fileName, accountBalanceList, PROCESSORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
