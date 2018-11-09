package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.mining.entity.Account;
import com.blockeng.mining.entity.AssetSnapshotDetail;
import com.blockeng.mining.mapper.AssetSnapshotDetailMapper;
import com.blockeng.mining.service.AccountService;
import com.blockeng.mining.service.AssetSnapshotDetailService;
import com.blockeng.mining.service.MineHelpService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description: 资产快照服务实现类
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:16
 * @Modified by: Chen Long
 */
@Service
@Slf4j
@Transactional
public class AssetSnapshotDetailServiceImpl extends ServiceImpl<AssetSnapshotDetailMapper, AssetSnapshotDetail> implements AssetSnapshotDetailService {


    @Autowired
    private AccountService accountService;

    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private MineHelpService mineHelpService;

    /**
     * 创建资产快照
     */
    @Override
    public boolean createSnapshotDetail( String yesterdayDate) {

        Long plantCoinId = Long.valueOf(configServiceClient.getConfig("SYSTEM", "PLATFORM_COIN_ID").getValue());//USDT

        List<Account> accounts = accountService.selectListByFlag();//获取当天所有余额不为0的,每天进行快照
        List<AssetSnapshotDetail> assetSnapshotDetails = new ArrayList<>(accounts.size());
        Map<Long, TradeMarketDTO> currentMarket = mineHelpService.getCurrentMarket();//获取市场上所有币种的对USDT成交均价

        for (Account account : accounts) {
            AssetSnapshotDetail assetSnapshotDetail = new AssetSnapshotDetail();
            TradeMarketDTO tradeMarketDTO = currentMarket.get(account.getCoinId());
            if (plantCoinId.equals(account.getCoinId())) {
                tradeMarketDTO = new TradeMarketDTO().setPrice(BigDecimal.ONE);
            }
            if (null == tradeMarketDTO) {
                log.error("不存在该交易对---coinID--"+account.getCoinId());
                continue;
            }
            BigDecimal totalAmount = BigDecimal.ZERO;
            totalAmount = totalAmount.add(account.getBalanceAmount()).add(account.getFreezeAmount());//每种币的账户总额以及对USDT成交均价
            try {
                assetSnapshotDetails.add(assetSnapshotDetail.
                        setFreezeAmount(account.getFreezeAmount()).
                        setBalanceAmount(account.getBalanceAmount()).
                        setCoinId(account.getCoinId()).
                        setBalance(totalAmount).
                        setUserId(account.getUserId()).
                        setPrice(tradeMarketDTO.getPrice()).
                        setSnapTime(yesterdayDate));
            } catch (Exception e) {
               log.error("异常---"+e.getMessage());
            }

        }
        return this.insertBatch(assetSnapshotDetails);
    }

}
