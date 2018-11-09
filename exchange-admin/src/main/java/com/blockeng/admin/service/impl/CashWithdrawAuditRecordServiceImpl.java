package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.entity.CashWithdrawAuditRecord;
import com.blockeng.admin.mapper.CashWithdrawAuditRecordMapper;
import com.blockeng.admin.service.CashWithdrawAuditRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提现审核记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class CashWithdrawAuditRecordServiceImpl extends ServiceImpl<CashWithdrawAuditRecordMapper, CashWithdrawAuditRecord> implements CashWithdrawAuditRecordService {

    /**
     * 法币提现审核记录
     *
     * @param orderId 法币提现订单号
     * @return
     */
    @Override
    public List<CashWithdrawAuditRecord> queryCashWithdrawAuditRecord(Long orderId) {
        EntityWrapper<CashWithdrawAuditRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", orderId).orderBy("created", false);
        return baseMapper.selectList(wrapper);
    }
}
