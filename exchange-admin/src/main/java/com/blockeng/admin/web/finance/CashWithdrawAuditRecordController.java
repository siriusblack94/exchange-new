package com.blockeng.admin.web.finance;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.CashWithdrawAuditRecord;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CashWithdrawAuditRecordService;
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
 * 提现审核记录 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/cash/withdraw/audit/record")
@Api(value = "法币提现审核记录", tags = {"法币提现审核记录"})
@Slf4j
public class CashWithdrawAuditRecordController {

    @Autowired
    private CashWithdrawAuditRecordService cashWithdrawAuditRecordService;

    /**
     * 法币提现审核记录
     *
     * @param orderId 法币提现订单号
     * @return
     */
    @Log(value = "法币提现审核记录", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_withdraw_audit_query')")
    @GetMapping("/{orderId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "法币提现审核记录", httpMethod = "GET")
    @ApiImplicitParam(name = "orderId", value = "法币提现订单号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ResultMap cashWithdrawAuditRecord(@PathVariable("orderId") Long orderId) {
        List<CashWithdrawAuditRecord> cashWithdrawAuditRecords = cashWithdrawAuditRecordService.queryCashWithdrawAuditRecord(orderId);
        return ResultMap.getSuccessfulResult(cashWithdrawAuditRecords);
    }
}
