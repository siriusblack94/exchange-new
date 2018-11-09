package com.blockeng.admin.web.finance;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.CoinWithdrawAuditRecord;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinWithdrawAuditRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 提币审核记录 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/coin/withdraw/audit/record")
@Api(value = "数字货币提现审核记录", tags = {"数字货币提现审核记录"})
@Slf4j
public class CoinWithdrawAuditRecordController {

    @Autowired
    private CoinWithdrawAuditRecordService coinWithdrawAuditRecordService;

    /**
     * 数字货币提现审核记录
     *
     * @param orderId 数字货币提现订单号
     * @return
     */
    @Log(value = "数字货币提现审核记录", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('coin_withdraw_query')")
    @GetMapping("/{orderId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "数字货币提现审核记录", httpMethod = "GET")
    @ApiImplicitParam(name = "orderId", value = "数字货币提现订单号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ResultMap coinWithdrawAuditRecord(@PathVariable("orderId") Long orderId) {
        List<CoinWithdrawAuditRecord> coinWithdrawAuditRecords = coinWithdrawAuditRecordService.coinWithdrawAuditRecord(orderId);
        return ResultMap.getSuccessfulResult(coinWithdrawAuditRecords);
    }
}

