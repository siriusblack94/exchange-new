package com.blockeng.mining.task;

import com.blockeng.mining.entity.PrivatePlacement;
import com.blockeng.mining.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:26
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class MiningTask {

    @Autowired
    private AssetSnapshotService assetSnapshotService;

    @Autowired
    private MineService mineService;

    @Autowired
    private DividendRecordService dividendRecordService;

    @Autowired
    private PlantCoinDividendRecordService plantCoinDividendRecordService;


    @Autowired
    private PrivatePlacementReleaseRecordService privatePlacementReleaseRecordService;

    @Autowired
    private PoolDividendRecordService poolDividendRecordService;


    /**
     * 每天统计一次挖矿
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void mining_1() {

//        定时任务1：快照账户资产(快照)
//        计算总资产 每个币换算成USDT的总和
        long start = System.currentTimeMillis();
        log.info("开始计算资产快照,time:" + start);
        assetSnapshotService.createAssetSnapshot();
        log.info("结束计算资产快照,time:" + (System.currentTimeMillis() - start));
//
//         定时任务2：统计当前参与交易的用户,并计算手续费(挖矿)
        start = System.currentTimeMillis();
        log.info("统计当前参与交易的用户,并计算手续费,time:" + start);
        mineService.calcTotalDayMining();
        log.info("结束统计当前参与交易的用户,并计算手续费,time:" + (System.currentTimeMillis() - start));
//
//
        start = System.currentTimeMillis();
        log.info("持有HB分红,time:" + start);
        plantCoinDividendRecordService.bxxDividend();
        log.info("结束持有HB分红,time:" + (System.currentTimeMillis() - start));

        //(私募释放)
        start = System.currentTimeMillis();
        log.info("私募释放开始,time:" + start);
        privatePlacementReleaseRecordService.release();
        log.info("私募释放结束,time:" + (System.currentTimeMillis() - start));


//         定时任务3：统计当天邀请奖励奖励(邀请)
        start = System.currentTimeMillis();
        log.info("统计当天邀请奖励奖励,time:" + start);
        dividendRecordService.inviteRelation();
        log.info("结束统计当天邀请奖励奖励,time:" + (System.currentTimeMillis() - start));



        start = System.currentTimeMillis();
        log.info("计算矿池当天分红,time:" + start);
        poolDividendRecordService.dividend();
        log.info("结束计算矿池当天分红,time:" + (System.currentTimeMillis() - start));


    }
}
