package com.blockeng.mining.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.http.Response;
import com.blockeng.mining.config.Constant;
import com.blockeng.mining.dto.FeeDTO;
import com.blockeng.mining.entity.MineData;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:14
 * @Modified by: Chen Long
 */
@Slf4j
@RestController
@RequestMapping("/mine/data")
@Api(value = "矿池数据", description = "矿池数据", tags = "矿池数据")
public class MineDataController {


    @Autowired
    private DividendRecordService dividendRecordService;

    @Autowired
    private PrivatePlacementReleaseRecordService privatePlacementReleaseRecordService;

    @Autowired
    private AssetSnapshotService assetSnapshotService;

    @Autowired
    private MineDataService mineDataService;
    @Autowired
    private MineService mineService;
    @Autowired
    private MineHelpService mineHelpService;
    @Autowired
    private PlantCoinDividendRecordService plantCoinDividendRecordService;
    @Autowired
    private MiningDetailService miningDetailService;
    @Autowired
    private ConfigServiceClient configServiceClient;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PoolDividendRecordService poolDividendRecordService;


    /**
     * 查询当日挖矿数据
     *
     * @return
     */
    @GetMapping
    @ApiOperation(value = "mine--002  查询挖矿信息", notes = "挖矿信息", httpMethod = "GET")
    public Response getMinerInfo() {
        String yesterdayDate = TimeUtils.getYesterdayDate();
        String nowDayDate = TimeUtils.getNowDay();
        BigDecimal realYesterdayMining =  mineService.dayTotalMine(yesterdayDate);//昨日真实挖矿数;
        if(realYesterdayMining==null ) realYesterdayMining= new BigDecimal(0);
        Map<String, BigDecimal> averagePriceMap = mineService.getAveragePrice();  //计算所有交易对HB的成交均价
        BigDecimal realNowDateMining = new BigDecimal(0);
        List<FeeDTO> feeDTOS = miningDetailService.dayTotalFee(nowDayDate);
        for (FeeDTO item : feeDTOS) { //计算当天挖矿总额度
            String areaName = item.getName().toUpperCase();   //HB/USDT  HB/BTC   HB/ETH
            BigDecimal HBPrice = averagePriceMap.get(areaName);
            if (null == HBPrice||HBPrice.compareTo(new BigDecimal(0))==0) {
                continue;
            }
            realNowDateMining = realNowDateMining.add(item.getTotal().divide(HBPrice, 8, RoundingMode.DOWN));//今日挖矿总量
        }
        String dividendRate = configServiceClient.getConfig("Mining", "DIVIDEND_RATE").getValue().toUpperCase();

        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        BigDecimal realYesterdayUSDT = new BigDecimal("0");
        if (realYesterdayMining.compareTo(new BigDecimal(0))>0){
            realYesterdayUSDT =  mineCurrentMarket.getPrice().multiply(realYesterdayMining);//昨日真实挖矿数对USDT价格;

        }

        BigDecimal realNowDateUSDT =  mineCurrentMarket.getPrice().multiply(realNowDateMining);//今日真实挖矿数对USDT价格;

        BigDecimal secIrculation = plantCoinDividendRecordService.totalDividend();//总流通量
        BigDecimal perMillionRevenue=BigDecimal.ZERO;
        if (secIrculation.compareTo(BigDecimal.ZERO)>0){
             perMillionRevenue = Constant.oneMillion.divide(secIrculation,8,RoundingMode.HALF_UP)
                    .multiply(new BigDecimal(dividendRate)).multiply(realNowDateUSDT);
        }
        QueryWrapper<MineData> ew = new QueryWrapper<>();
        ew.between("statistics_date",nowDayDate+" 00:00:00",nowDayDate+" 23:59:59");
        MineData mineData = mineDataService.selectOne(ew);
        if (mineData==null) mineData= new MineData();
        mineData.setPreMining(realYesterdayMining).setPreDistributed(realYesterdayUSDT)
        .setDistributed(realNowDateUSDT).setSecIrculation(secIrculation).setPerMillionRevenue(perMillionRevenue).setStatisticsDate(new Date());
        mineDataService.insertOrUpdate(mineData);
        return Response.ok(mineData);
    }

//    @GetMapping("/change")
//    public String  change(){
//
//        mineDataService.change();
//        return "ok";
//    }

//    @GetMapping("/task")
//    public String  task(){
//        long start = System.currentTimeMillis();
//        log.info("开始计算资产快照,time:" + start);
//        assetSnapshotService.createAssetSnapshot();
//        log.info("结束计算资产快照,time:" + (System.currentTimeMillis() - start));
////
////       定时任务2：统计当前参与交易的用户,并计算手续费(挖矿)
//        start = System.currentTimeMillis();
//        log.info("统计当前参与交易的用户,并计算手续费,time:" + start);
//        mineService.calcTotalDayMining();
//        log.info("结束统计当前参与交易的用户,并计算手续费,time:" + (System.currentTimeMillis() - start));
//
//        //(私募释放)
//        start = System.currentTimeMillis();
//        log.info("私募释放开始,time:" + start);
//        privatePlacementReleaseRecordService.release();
//        log.info("私募释放结束,time:" + (System.currentTimeMillis() - start));
//
////         定时任务3：统计当天邀请奖励奖励(邀请)
//        start = System.currentTimeMillis();
//        log.info("统计当天邀请奖励奖励,time:" + start);
//        dividendRecordService.inviteRelation();
//        log.info("结束统计当天邀请奖励奖励,time:" + (System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        log.info("持有HB分红,time:" + start);
//        plantCoinDividendRecordService.bxxDividend();
//        log.info("结束持有HB分红,time:" + (System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        log.info("计算矿池当天分红,time:" + start);
//        poolDividendRecordService.dividend();
//        log.info("结束计算矿池当天分红,time:" + (System.currentTimeMillis() - start));
//        return "ok";
//    }

}
