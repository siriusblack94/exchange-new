package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.CoinDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.dto.FeeDTO;
import com.blockeng.mining.dto.PlantCoinDividendRecordDTO;
import com.blockeng.mining.dto.PlantCoinDividendTotalDTO;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.mapper.PlantCoinDividendRecordMapper;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@Transactional
public class PlantCoinDividendRecordServiceImpl extends ServiceImpl<PlantCoinDividendRecordMapper, PlantCoinDividendRecord> implements PlantCoinDividendRecordService {


    @Autowired
    private AssetSnapshotDetailService assetSnapshotDetailService;


    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private MineService mineService;

    @Autowired
    private MiningDetailService miningDetailService;

    @Autowired
    private MineHelpService mineHelpService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 持有bxx分红
     */
    @Override
    public void bxxDividend() {

        String yesterdayDate = TimeUtils.getYesterdayDate();
        String nowDay = TimeUtils.getNowDay();


        if (null != this.baseMapper.selectOne(new QueryWrapper<PlantCoinDividendRecord>().eq("reward_date", nowDay).last("limit 1"))) {//已经快照过了
            log.info("当天分红已经计算过,不能重复快照");
            return;
        }
        Long mineCoinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue().toUpperCase());//获取挖矿交易名称

        if ( mineCoinId <= 0L) {
            log.error("未配置挖矿币种id");
            return;
        }
        String dividendRate = configServiceClient.getConfig("Mining", "DIVIDEND_RATE").getValue().toUpperCase();//分红占当前账户的比例

        BigDecimal totalLiquidity = totalDividend();//平台的总流通量
        List<FeeDTO> feeDTOList = miningDetailService.dayTotalFee(yesterdayDate);//每个区域的总手续费
        if (CollectionUtils.isEmpty(feeDTOList)||BigDecimal.ZERO.compareTo(totalLiquidity)==0) {
            log.error("不存在前一天的挖矿信息");
            return;
        }
        QueryWrapper<AssetSnapshotDetail> ew = new QueryWrapper<>();
        ew.eq("coin_id", mineCoinId);
        ew.eq("snap_time", TimeUtils.getYesterdayDate());
        List<AssetSnapshotDetail> assetSnapshotDetails = assetSnapshotDetailService.selectList(ew);

        for (FeeDTO feeDTO : feeDTOList) {
            feeDTO.setTotal(feeDTO.getTotal().multiply(new BigDecimal(dividendRate)));
            log.info("--name--"+feeDTO.getName()+"--Total--"+feeDTO.getTotal());
        }

        if (!CollectionUtils.isEmpty(assetSnapshotDetails)) {
            List<PlantCoinDividendRecord> userDividendList = new ArrayList<>(assetSnapshotDetails.size() * feeDTOList.size());
            for (AssetSnapshotDetail item : assetSnapshotDetails) {
                BigDecimal totalBalance = BigDecimal.ZERO;
                //当前持有分红的总的额度
                totalBalance = totalBalance.add(item.getBalanceAmount()).add(item.getFreezeAmount());
                //个人持平台币占总量的比例，下面用来做分红计算
                BigDecimal userDividend = totalBalance.divide(totalLiquidity, 8, RoundingMode.DOWN);
                log.info("用户---"+item.getUserId()+"---比例----"+userDividend);
                //将各个交易区的手续费进行分红
                for (FeeDTO feeDTO : feeDTOList) {
                    userDividendList.add(new PlantCoinDividendRecord()
                            .setCoinName(feeDTO.getName()).
                            setAmount(userDividend.multiply(feeDTO.getTotal())).
                            setRewardDate(nowDay).
                            setUserId(item.getUserId()));
                }
            }
            this.insertBatch(userDividendList);//记录当天参入分红的详情

            Map<String, CoinDTO> allCoin = mineHelpService.getAllCoin();
            for (PlantCoinDividendRecord item : userDividendList) {//开始分红到每个账户
                String coinName = item.getCoinName();
                CoinDTO coinDTO = allCoin.get(coinName);
                if (null == coinDTO) {//不存在当前币种信息
                    log.error("未查询到当前币种信息");
                    continue;
                }
                Long id = coinDTO.getId();
                UnlockDTO unlockDTO = new UnlockDTO().
                        setCoinId(id).
                        setAmount(item.getAmount()).
                        setBusinessType(BusinessType.PLANT_COIN_DIG).
                        setDesc(BusinessType.PLANT_COIN_DIG.getDesc()).
                        setUserId(item.getUserId()).
                        setOrderId(item.getId());
                log.info("----unlockDTO-----"+ GsonUtil.toJson(unlockDTO));
                rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));

            }
        } else {
            log.error("未找到当天资产快照中持有bxx的账户");
        }
    }

    @Override
    public BigDecimal totalDividend() {
        String mineRate = configServiceClient.getConfig("Mining", "MINE_RATE").getValue().toUpperCase();//挖矿占流通量比例
        BigDecimal totalMine = mineService.totalMine();
        if (null == totalMine) {
            return BigDecimal.ZERO;
        }
        return totalMine.divide(new BigDecimal(mineRate), 8, RoundingMode.DOWN);
    }

    @Override
    public Object plantCoinDividendRecord(Page<PlantCoinDividendRecord> page, Long id) {
        List<PlantCoinDividendRecordDTO> plantCoinDividendRecordDTOS= new ArrayList<>();
        QueryWrapper<PlantCoinDividendRecord> qw = new QueryWrapper<>();
        qw.select("DISTINCT(reward_date)");
        qw.eq("user_id",id);
        IPage<PlantCoinDividendRecord> plantCoinDividendRecordIPage = this.baseMapper.selectPage(page, qw);
        for (PlantCoinDividendRecord item1 : plantCoinDividendRecordIPage.getRecords()) {
            PlantCoinDividendRecordDTO plantCoinDividendRecordDTO = new PlantCoinDividendRecordDTO();
            plantCoinDividendRecordDTO.setRewardDate(item1.getRewardDate());
            QueryWrapper<PlantCoinDividendRecord> ew = new QueryWrapper<>();
            ew.eq("user_id", id);
            ew.eq("reward_date",item1.getRewardDate());
            ew.groupBy("coin_name");
            List<PlantCoinDividendTotalDTO> plantCoinDividendTotalDTOS = this.baseMapper.selectTotalAmount(ew);
            if (!CollectionUtils.isEmpty(plantCoinDividendTotalDTOS)) {
                for (PlantCoinDividendTotalDTO item : plantCoinDividendTotalDTOS) {
                    item.setUsdtAccount(mineHelpService.getUsdtAmount(item.getCoinName(), item.getAmount()));
                    item.setCnyAccount(mineHelpService.getCnyAmount(item.getCoinName(), item.getAmount()));
                    plantCoinDividendRecordDTO.setCnyAccount(item.getCnyAccount()
                            .add(plantCoinDividendRecordDTO.getCnyAccount()==null? BigDecimal.ZERO:plantCoinDividendRecordDTO.getCnyAccount()));
                    plantCoinDividendRecordDTO.setUsdtAccount(item.getUsdtAccount()
                            .add(plantCoinDividendRecordDTO.getUsdtAccount()==null?BigDecimal.ZERO:plantCoinDividendRecordDTO.getUsdtAccount()));
                }
            }
            plantCoinDividendRecordDTO.setPlantCoinDividendDetails(plantCoinDividendTotalDTOS);
            plantCoinDividendRecordDTOS.add(plantCoinDividendRecordDTO);
        }
        IPage<PlantCoinDividendRecordDTO> plantCoinDividendRecordDTOIPage = new Page<>();
        plantCoinDividendRecordDTOIPage.setRecords(plantCoinDividendRecordDTOS)
                .setTotal(plantCoinDividendRecordIPage.getTotal())
                .setSize(plantCoinDividendRecordIPage.getSize())
                .setCurrent(plantCoinDividendRecordIPage.getCurrent());
        return Response.ok(plantCoinDividendRecordDTOIPage);
    }

    @Override
    public Object plantCoinDividendTotal(Long id) {
        QueryWrapper<PlantCoinDividendRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", id);
        ew.groupBy("coin_name");
        List<PlantCoinDividendTotalDTO> plantCoinDividendTotalDTOS = this.baseMapper.selectTotalAmount(ew);
        if (!CollectionUtils.isEmpty(plantCoinDividendTotalDTOS)) {
            for (PlantCoinDividendTotalDTO item : plantCoinDividendTotalDTOS) {
                item.setUsdtAccount(mineHelpService.getUsdtAmount(item.getCoinName(), item.getAmount()));
                item.setCnyAccount(mineHelpService.getCnyAmount(item.getCoinName(), item.getAmount()));
            }
        }
        return plantCoinDividendTotalDTOS;
    }


}
