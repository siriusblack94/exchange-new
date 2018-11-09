package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.entity.PrivatePlacement;
import com.blockeng.mining.entity.PrivatePlacementReleaseRecord;
import com.blockeng.mining.mapper.*;
import com.blockeng.mining.service.PrivatePlacementReleaseRecordService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @Auther: sirius
 * @Date: 2018/8/15 13:33
 * @Description:
 */
@Service
@Slf4j
public class PrivatePlacementReleaseRecordServiceImpl  extends ServiceImpl<PrivatePlacementReleaseRecordMapper, PrivatePlacementReleaseRecord> implements PrivatePlacementReleaseRecordService {
    @Autowired
    private PrivatePlacementMapper privatePlacementMapper;
    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MineMapper mineMapper;

    @Override
    public void release() {
        String yesterdayDate = TimeUtils.getYesterdayDate();
        String nowDay = TimeUtils.getNowDay();
        if (null != this.baseMapper.selectOne(new QueryWrapper<PrivatePlacementReleaseRecord>().between("created", nowDay+" 00:00:01",nowDay+" 23:59:59").last("limit 1"))) {//挖矿数据已经统计
            log.error("当天私募已经释放");
            return;
        }

        BigDecimal releaseRateConfig = new BigDecimal(configServiceClient.getConfig("MINING", "RELEASE_RATE").getValue().toUpperCase());//获取私募释放总比例
        BigDecimal mineRateConfig  = new BigDecimal(configServiceClient.getConfig("MINING", "MINE_RATE").getValue().toUpperCase());//获取挖矿释放总比例
        Long plantCoinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue());//获取挖矿币ID

        BigDecimal rate = releaseRateConfig.divide(mineRateConfig,8,RoundingMode.HALF_UP);//获取挖矿释放总比例
        BigDecimal realMining =  mineMapper.dayTotalMine(yesterdayDate);//昨日真实挖矿数
        if (realMining==null||realMining.compareTo(BigDecimal.ZERO)<=0)
        {
            log.error("昨日无挖矿数据");
            return;
        }
        BigDecimal releaseTotalCount = realMining.multiply(rate).setScale(8,RoundingMode.HALF_UP) ;//今日释放总数量
        BigDecimal totalAmount =privatePlacementMapper.totalAmount();//私募总资产
        QueryWrapper<PrivatePlacement> qw = new QueryWrapper<>();
        qw.gt("freeze_amount",0);
        List<PrivatePlacement> privatePlacements = privatePlacementMapper.selectList(qw);
        for (PrivatePlacement privatePlacement : privatePlacements) {
            BigDecimal releaseRate = privatePlacement.getAmount().divide(totalAmount,8,RoundingMode.HALF_UP);//当前用户释放比例
            BigDecimal releaseCount=releaseTotalCount.multiply(releaseRate).setScale(8,RoundingMode.HALF_UP);//当前用户释放数量
            if (privatePlacement.getFreezeAmount().compareTo(releaseCount)<=0)//冻结额小于释放量取冻结额
                releaseCount=privatePlacement.getFreezeAmount();
            PrivatePlacementReleaseRecord privatePlacementReleaseRecord = new PrivatePlacementReleaseRecord();
            privatePlacementReleaseRecord.setReleaseAmountRate(releaseRate).setUserId(privatePlacement.getUserId())
                    .setReleaseAmount(releaseCount);//当次释放记录
            BigDecimal   releaseAmountTotal  =releaseCount.add(privatePlacement.getReleaseAmount());//当前用户总释放量
            privatePlacement.setReleaseAmount(releaseAmountTotal)
                    .setFreezeAmount(privatePlacement.getAmount().subtract(releaseAmountTotal)).setLastUpdateTime(new Date());
            //更新私募表和插入释放表，成功后添加该用户余额和资金流水
            if ((privatePlacementMapper.updateById(privatePlacement)==1)&&(baseMapper.insert(privatePlacementReleaseRecord) == 1)){
                UnlockDTO unlockDTO = new UnlockDTO().
                        setCoinId(plantCoinId).
                        setAmount( privatePlacementReleaseRecord.getReleaseAmount()).
                        setBusinessType( BusinessType.RELEASE).
                        setDesc(BusinessType.RELEASE.getDesc()).
                        setUserId(privatePlacement.getUserId()).
                        setOrderId(privatePlacementReleaseRecord.getId());
                log.info("----unlockDTO-----"+ GsonUtil.toJson(unlockDTO));
                rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));


            }
        }
    }
}
