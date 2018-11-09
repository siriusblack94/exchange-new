package com.blockeng.mining.service.impl;


import afu.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.framework.http.Response;
import com.blockeng.mining.dto.NowWeekDividendDTO;
import com.blockeng.mining.dto.PoolDividendAccountDTO;
import com.blockeng.mining.dto.PoolDividendTotalAccountDTO;
import com.blockeng.mining.entity.MinePool;
import com.blockeng.mining.entity.PoolDividendAccount;
import com.blockeng.mining.entity.PoolDividendRecord;
import com.blockeng.mining.mapper.PoolDividendAccountMapper;
import com.blockeng.mining.service.MineHelpService;
import com.blockeng.mining.service.MinePoolService;
import com.blockeng.mining.service.PoolDividendAccountService;
import com.blockeng.mining.service.PoolDividendRecordService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class PoolDividendAccountServiceImpl extends ServiceImpl<PoolDividendAccountMapper, PoolDividendAccount> implements PoolDividendAccountService {

    @Autowired
    private MineHelpService mineHelpService;


    @Autowired
    private PoolDividendRecordService poolDividendRecordService;

    @Autowired
    private MinePoolService minePoolService;


    @Override
    public Object poolAccountList(Page<PoolDividendAccount> page, Long id) {
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();

        IPage<PoolDividendAccount> poolDividendAccountPage = this.baseMapper.selectPage(page, new QueryWrapper<PoolDividendAccount>().eq("user_id", id));
        Page<PoolDividendAccountDTO> poolDividendAccountDTOPage = new Page<>();
        poolDividendAccountDTOPage.setCurrent(poolDividendAccountPage.getCurrent()).
                setTotal(poolDividendAccountPage.getTotal()).setSize(poolDividendAccountPage.getSize()).setRecords(new ArrayList<>());
        if (!CollectionUtils.isEmpty(poolDividendAccountPage.getRecords())) {
            for (PoolDividendAccount item : poolDividendAccountPage.getRecords()) {
                poolDividendAccountDTOPage.getRecords().add(new PoolDividendAccountDTO().
                        setRewardAmount(item.getRewardAmount()).
                        setLockAmount(item.getLockAmount()).
                        setUnlockAmount(item.getUnlockAmount()).
                        setUnlockDate(item.getUnlockDate()).
                        setCnyAccount(item.getRewardAmount().multiply(mineCurrentMarket.getCnyPrice())).
                        setUsdtAccount(item.getRewardAmount().multiply(mineCurrentMarket.getPrice())));
            }
        }
        return Response.ok(poolDividendAccountDTOPage);
    }

    @Override
    public PoolDividendTotalAccountDTO selectPriTotalUnAcount(Long id) {
        return baseMapper.selectTotal(id);
    }

    @Override
    public Object dividendAccountTotal(Long id) {
        List<MinePool> minePools = minePoolService.selectList(new QueryWrapper<MinePool>().eq("create_user", id));
        if (null == minePools||minePools.size()==0) {
            return Response.err(1000011, "当前用户不是矿主");
        }
        PoolDividendTotalAccountDTO poolDividendTotalAccountDTO = this.baseMapper.selectTotal(id);
        if (null != poolDividendTotalAccountDTO) {
            TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
            BigDecimal totalRewardAmount = poolDividendTotalAccountDTO.getTotalRewardAmount();
            poolDividendTotalAccountDTO.setCnyAmount(totalRewardAmount.multiply(mineCurrentMarket.getPrice()))
                    .setUsdtAmount(totalRewardAmount.multiply(mineCurrentMarket.getCnyPrice()));
            BigDecimal poolTotalHold = poolDividendRecordService.getTotalUserHold(id, poolDividendRecordService.getPoolAllUser(id));
            poolDividendTotalAccountDTO.setTotalHold(poolTotalHold);
        }

        return poolDividendTotalAccountDTO;
    }


}
