package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.entity.CashRechargeAuditRecord;
import com.blockeng.admin.mapper.CashRechargeAuditRecordMapper;
import com.blockeng.admin.service.CashRechargeAuditRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 充值审核记录表 服务实现类
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
@Service
public class CashRechargeAuditRecordServiceImpl extends ServiceImpl<CashRechargeAuditRecordMapper, CashRechargeAuditRecord> implements CashRechargeAuditRecordService {

    /**
     * 法币充值审核记录
     *
     * @param orderId 法币充值订单号
     * @return
     */
    @Override
    public List<CashRechargeAuditRecord> queryCashRechargeAuditRecord(Long orderId) {
        EntityWrapper<CashRechargeAuditRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", orderId).orderBy("created", false);
        return baseMapper.selectList(wrapper);
    }
}
