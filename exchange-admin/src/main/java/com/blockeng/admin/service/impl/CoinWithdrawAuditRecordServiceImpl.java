package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.entity.CoinWithdrawAuditRecord;
import com.blockeng.admin.mapper.CoinWithdrawAuditRecordMapper;
import com.blockeng.admin.service.CoinWithdrawAuditRecordService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 提币审核记录 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class CoinWithdrawAuditRecordServiceImpl extends ServiceImpl<CoinWithdrawAuditRecordMapper, CoinWithdrawAuditRecord> implements CoinWithdrawAuditRecordService {

    /**
     * 数字货币提现审核记录
     *
     * @param orderId 数字货币提现订单号
     * @return
     */
    @Override
    public List<CoinWithdrawAuditRecord> coinWithdrawAuditRecord(Long orderId) {
        EntityWrapper<CoinWithdrawAuditRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("order_id", orderId).orderBy("created", false);
        return baseMapper.selectList(wrapper);
    }
}
