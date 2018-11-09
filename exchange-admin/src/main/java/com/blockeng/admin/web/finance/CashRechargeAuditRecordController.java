package com.blockeng.admin.web.finance;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.CashRechargeAuditRecord;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CashRechargeAuditRecordService;
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
 * @Description: 法币充值审核记录 Controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/27 下午4:36
 * @Modified by: Chen Long
 */
@Api(value = "法币充值审核记录", tags = {"法币充值审核记录"})
@RestController
@RequestMapping("/cash/recharge/audit/record")
@Slf4j
public class CashRechargeAuditRecordController {

    @Autowired
    private CashRechargeAuditRecordService cashRechargeAuditRecordService;

    /**
     * 法币充值审核记录
     *
     * @param orderId 法币充值订单号
     * @return
     */
    @Log(value = "法币充值审核记录", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('cash_recharge_audit_query')")
    @GetMapping("/{orderId}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "法币充值审核记录", httpMethod = "GET")
    @ApiImplicitParam(name = "orderId", value = "法币充值订单号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ResultMap cashRechargeAuditRecord(@PathVariable("orderId") Long orderId) {
        List<CashRechargeAuditRecord> cashRechargeAuditRecords = cashRechargeAuditRecordService.queryCashRechargeAuditRecord(orderId);
        return ResultMap.getSuccessfulResult(cashRechargeAuditRecords);
    }
}
