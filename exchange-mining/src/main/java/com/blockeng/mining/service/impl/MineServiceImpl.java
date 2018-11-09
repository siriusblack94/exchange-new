package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.feign.TradingAreaServiceClient;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.config.Constant;
import com.blockeng.mining.dto.MineCoinInfoDTO;
import com.blockeng.mining.dto.MineTotalDTO;
import com.blockeng.mining.entity.Account;
import com.blockeng.mining.entity.AssetSnapshot;
import com.blockeng.mining.entity.Mine;
import com.blockeng.mining.entity.MiningDetail;
import com.blockeng.mining.mapper.MineMapper;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 挖矿统计
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */

@Service
@Slf4j
@Transactional
public class MineServiceImpl extends ServiceImpl<MineMapper, Mine> implements MineService {


    @Autowired
    private RedissonClient redisson;

    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private TradingAreaServiceClient tradingAreaServiceClient;

    @Autowired
    private MiningDetailService miningDetailService;

    @Autowired
    private AssetSnapshotService assetSnapshotService;

    @Autowired
    private MineHelpService mineHelpService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AccountService accountService;

    /**
     * 根据当天用户汇总数据,计算分红
     */
    @Override
    public void calcTotalDayMining() {


        String yesterdayDate = TimeUtils.getYesterdayDate();
        String beforeYesterdayDate =TimeUtils.get2DdayBeforeDate();
        if (null != this.baseMapper.selectOne(new QueryWrapper<Mine>().eq("time_mining", yesterdayDate).last("limit 1"))) {//挖矿数据已经统计
            log.info("当天挖矿数据已经统计");
            return;
        }

        String miningRate = configServiceClient.getConfig("Mining", "MINING_RATE").getValue();//交易挖矿返还比例
        String miningAccountRate = configServiceClient.getConfig("Mining", "MINING_ACCOUNT_RATE").getValue();//资产上线比例

        Map<String, BigDecimal> averagePriceMap = getAveragePrice();  //获取前一天每个交易的成交均价  HB/USDT  HB/BTC   HB/ETH

        //获取当天的所有挖矿数据
        QueryWrapper<MiningDetail> ew = new QueryWrapper<>();
        ew.eq("time_mining", yesterdayDate);
        List<MiningDetail> miningDetails = miningDetailService.selectList(ew);
        if (CollectionUtils.isEmpty(miningDetails)) {
            log.error("当前不存在挖矿数据");
            return;
        }
        Map<Long, List<MiningDetail>> userMiningMap = miningDetails.stream().collect(Collectors.groupingBy(MiningDetail::getUserId));


        //获取每个用户前一日的资产快照,并且按照用户分组
        QueryWrapper<AssetSnapshot> assetEw = new QueryWrapper<>();
        assetEw.eq("snap_time", yesterdayDate);
        List<AssetSnapshot> assetSnapshotList = assetSnapshotService.selectList(assetEw);
        Map<Long, AssetSnapshot> userAssetSnapshotMap = new HashMap<>(assetSnapshotList.size());
        for (AssetSnapshot assetSnapshot : assetSnapshotList) {
            userAssetSnapshotMap.put(assetSnapshot.getUserId(), assetSnapshot);
        }
        List<Mine> mineList = new ArrayList<>(userMiningMap.size());//初始化
        BigDecimal mineCoinUsdtPrice = averagePriceMap.get(Constant.USDT);//HB对USDT的价格
        for (Map.Entry<Long, List<MiningDetail>> entry : userMiningMap.entrySet()) {
            Long userId = entry.getKey();
            List<MiningDetail> miningList = entry.getValue();
            BigDecimal realTotalBxxTotalVolume = BigDecimal.ZERO;
            for (MiningDetail item : miningList) { //计算单个用户挖矿总额度
                String areaName = item.getAreaName().toUpperCase();   //HB/USDT  HB/BTC   HB/ETH
                BigDecimal bxxPrice = averagePriceMap.get(areaName);
                if (null == bxxPrice||bxxPrice.compareTo(new BigDecimal(0))==0) {
                    log.error("计算挖矿数据异常,ID:" + item.getId());
                    continue;
                }
                realTotalBxxTotalVolume = realTotalBxxTotalVolume.add(item.getTotalFee().divide(bxxPrice, 8, RoundingMode.DOWN));//挖矿总量
            }
            BigDecimal bxxTotalVolume = realTotalBxxTotalVolume.multiply(new BigDecimal(miningRate));
            AssetSnapshot assetSnapshot = userAssetSnapshotMap.get(userId);
            if (null != assetSnapshot && assetSnapshot.getBalance().compareTo(BigDecimal.ZERO) > 0) {//如果参入了挖矿,但是在前一日就提走了所有的款,不在进行挖矿计算
                BigDecimal maxMiningBalance = assetSnapshot.getBalance().divide(mineCoinUsdtPrice, 8, RoundingMode.DOWN);//当前余额折算平台币
                //如果当天刷掉全部手续费余额是0,返手续费一半
                maxMiningBalance= bxxTotalVolume.add(maxMiningBalance).multiply(new BigDecimal(miningAccountRate)).setScale(8,RoundingMode.HALF_UP);
                BigDecimal realMiningBalance = bxxTotalVolume.compareTo(maxMiningBalance) > 0 ? maxMiningBalance : bxxTotalVolume;
                mineList.add(new Mine().setTimeMining(yesterdayDate).setUserId(userId).setTotalMining(realTotalBxxTotalVolume).setRealMining(realMiningBalance));
            } else {
                log.error("前一天账户余额不足--userId:" + (null == assetSnapshot ? userId : assetSnapshot.getBalance()));
            }
        }
        this.insertBatch(mineList);//插入挖矿数据总和
        Long mineCoinId = mineHelpService.getMineCoinId();
        for (Mine mine : mineList) {//遍历当天挖矿所有数据
            UnlockDTO unlockDTO = new UnlockDTO().
                    setCoinId(mineCoinId).
                    setUserId(mine.getUserId()).
                    setBusinessType(BusinessType.MINE_DIG).
                    setOrderId(mine.getId()).
                    setAmount(mine.getRealMining()).
                    setDesc(BusinessType.MINE_DIG.getDesc());
            log.info("pool.unlock---unlockDTO：" + GsonUtil.toJson(unlockDTO));
            rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));

        }
    }


    /**
     * 获取平台总的挖矿额度
     */
    @Override
    public BigDecimal totalMine() {
        return this.baseMapper.totalMine();
    }

    /**
     * 获取按照指定日期平台总的挖矿额度
     */
    @Override
    public BigDecimal dayTotalMine(String timeMining) {
        return this.baseMapper.dayTotalMine(timeMining);
    }

    @Override
    public MineTotalDTO mineTotal(Long id) {
        QueryWrapper<Mine> ew = new QueryWrapper<>();
        ew.eq("user_id", id);
        BigDecimal totalMining = this.baseMapper.mineTotal(ew);
        if (totalMining == null) return new MineTotalDTO();
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        BigDecimal mineToCny =  mineCurrentMarket.getCnyPrice();
        BigDecimal mineToUsdt =  mineCurrentMarket.getPrice();
        BigDecimal mineCny = totalMining.multiply(mineToCny).setScale(8, RoundingMode.HALF_UP);
        BigDecimal mineUsdt = totalMining.multiply(mineToUsdt).setScale(8, RoundingMode.HALF_UP);
        return new MineTotalDTO().setMineCny(mineCny).setTotalCoin(totalMining).setMineUsdt(mineUsdt);
    }

    /**
     * 获取上个月挖矿总额度
     *
     * @return
     */
    @Override
    public BigDecimal priMonthTotalMine(String day, Long userId) {
        QueryWrapper<Mine> qw = new QueryWrapper<>();
        qw.select("sum(`real_mining`)");
        qw.between("time_mining", TimeUtils.getPriMonethFirst(day), TimeUtils.getPriMonethLast(day));
        qw.eq("user_id", userId);
        return super.baseMapper.mineTotal(qw);
    }

    @Override
    public BigDecimal priWeekTotalMine(String nowDay, Long id) {
        QueryWrapper<Mine> qw = new QueryWrapper<>();
        qw.select("sum(`real_mining`)");
        qw.between("time_mining", TimeUtils.getWeekPriFirst(nowDay), TimeUtils.getWeekPriLast(nowDay));
        qw.eq("user_id", id);
        return super.baseMapper.mineTotal(qw);
    }

    @Override
    public Object mineCoinInfo(Long id) {
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        MineCoinInfoDTO mineCoinInfoDTO = new MineCoinInfoDTO();
        Account mineCoinInfo = accountService.getMineCoinInfo(id);
        mineCoinInfoDTO.setBalanceAmount(mineCoinInfo.getBalanceAmount());
        mineCoinInfoDTO.setFreezeAmount(mineCoinInfo.getFreezeAmount());
        mineCoinInfoDTO.setAmount(mineCoinInfoDTO.getBalanceAmount().add(mineCoinInfoDTO.getFreezeAmount()));
        mineCoinInfoDTO.setCnyAccount(mineCurrentMarket.getCnyPrice().multiply(mineCoinInfoDTO.getAmount()));
        mineCoinInfoDTO.setUsdtAccount(mineCurrentMarket.getPrice().multiply(mineCoinInfoDTO.getAmount()));
        return mineCoinInfoDTO;
    }

    /**
     * 计算所有交易对bxx的成交均价
     */
    @Override
    public Map<String, BigDecimal> getAveragePrice() {
        Map<String, BigDecimal> averagePriceMap = new HashMap<>();

        String miningName = configServiceClient.getConfig("Mining", "COIN_NAME").getValue().toUpperCase();//获取挖矿交易名称
        RMap<Long, TradeMarketDTO> marketMap = redisson.getMap("MARKET_CACHE");
        Collection<TradeMarketDTO> values = marketMap.values();//获取所有交易对信息
        Map<String, TradeMarketDTO> symbolMarketMap = new HashMap<>();
        for (TradeMarketDTO tradeMarketDTO : values) {
            symbolMarketMap.put(tradeMarketDTO.getSymbol(), tradeMarketDTO);
        }

        List<TradeAreaDTO> tradeAreaDTOS = tradingAreaServiceClient.tradingAreaList();//获取所以交易区域
        for (TradeAreaDTO tradeMarketDTO : tradeAreaDTOS) {
            String name = tradeMarketDTO.getName().toUpperCase();
            String miningSymbol = miningName + name;//获取挖矿交易的sysbol   HB/USDT  HB/BTC   HB/ETH
            TradeMarketDTO miningTradeMarket = symbolMarketMap.get(miningSymbol);
            BigDecimal average;
            if (null == miningTradeMarket) {
                if (miningName.equals(name)){
                    average=BigDecimal.ONE;
                }else {
                log.error("未配置当前交易对" + miningSymbol);
                continue;
                }
            }else {
            //当前交易对的日总成交额除以总量
                if (miningTradeMarket.getAmount().compareTo(BigDecimal.ZERO) > 0 && miningTradeMarket.getVolume().compareTo(BigDecimal.ZERO) > 0) {
                    average = miningTradeMarket.getAmount().divide(miningTradeMarket.getVolume(), 8, RoundingMode.DOWN);
                } else {
                    average = miningTradeMarket.getPrice();
                }
            }
            averagePriceMap.put(name, average);
        }
        return averagePriceMap;
    }
}
