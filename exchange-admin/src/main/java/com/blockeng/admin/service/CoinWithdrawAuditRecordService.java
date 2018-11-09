package com.blockeng.admin.service;

import com.blockeng.admin.entity.CoinWithdrawAuditRecord;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 提币审核记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface CoinWithdrawAuditRecordService extends IService<CoinWithdrawAuditRecord> {

    /**
     * 数字货币提现审核记录
     *
     * @param orderId 数字货币提现订单号
     * @return
     */
    List<CoinWithdrawAuditRecord> coinWithdrawAuditRecord(Long orderId);
}
