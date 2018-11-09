package com.blockeng.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.CashRechargeAuditRecord;

import java.util.List;

/**
 * <p>
 * 充值审核记录表 服务类
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
public interface CashRechargeAuditRecordService extends IService<CashRechargeAuditRecord> {

    /**
     * 法币充值审核记录
     *
     * @param orderId 法币充值订单号
     * @return
     */
    List<CashRechargeAuditRecord> queryCashRechargeAuditRecord(Long orderId);
}
