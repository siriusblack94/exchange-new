package com.blockeng.admin.service;

import com.blockeng.admin.entity.CashWithdrawAuditRecord;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 提现审核记录 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface CashWithdrawAuditRecordService extends IService<CashWithdrawAuditRecord> {

    /**
     * 法币提现审核记录
     *
     * @param orderId 法币提现订单号
     * @return
     */
    List<CashWithdrawAuditRecord> queryCashWithdrawAuditRecord(Long orderId);
}
