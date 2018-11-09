package com.blockeng.mining.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.mining.entity.AssetSnapshot;
import com.blockeng.mining.entity.AssetSnapshotDetail;
import com.blockeng.mining.mapper.AssetSnapshotMapper;
import com.blockeng.mining.service.AssetSnapshotDetailService;
import com.blockeng.mining.service.AssetSnapshotService;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 资产快照服务实现类
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 上午12:16
 * @Modified by: Chen Long
 */
@Service
@Slf4j
@Transactional
public class AssetSnapshotServiceImpl extends ServiceImpl<AssetSnapshotMapper, AssetSnapshot> implements AssetSnapshotService {


    @Autowired
    private AssetSnapshotDetailService assetSnapshotDetailService;

    /**
     * 创建总的资产快照
     */
    @Override
    public boolean createAssetSnapshot() {
        String yesterdayDate = TimeUtils.getYesterdayDate();
        if (null != this.baseMapper.selectOne(new QueryWrapper<AssetSnapshot>().eq("snap_time", yesterdayDate).last("limit 1"))) {//已经快照过了
            log.info("当天已经快照过,不能重复快照");
            return true;
        }
        if (null == assetSnapshotDetailService.selectOne(new QueryWrapper<AssetSnapshotDetail>().eq("snap_time", yesterdayDate).last("limit 1")))//不为空说明已经快照过
        {
            log.info("开始创建快照明细");
            assetSnapshotDetailService.createSnapshotDetail(yesterdayDate);
        }
            //当天资产快照
            QueryWrapper<AssetSnapshotDetail> ew = new QueryWrapper<>();
            ew.eq("snap_time", yesterdayDate);
            List<AssetSnapshotDetail> assetSnapshotDetails = assetSnapshotDetailService.selectList(ew);
            Map<Long, List<AssetSnapshotDetail>> accountMap = assetSnapshotDetails.stream().collect(Collectors.groupingBy(AssetSnapshotDetail::getUserId));
            List<AssetSnapshot> snapshots = new ArrayList<>(accountMap.size());
            for (Map.Entry<Long, List<AssetSnapshotDetail>> entry : accountMap.entrySet()) {
                Long userId = entry.getKey();
                List<AssetSnapshotDetail> accountList = entry.getValue();
                BigDecimal totalBalance = BigDecimal.ZERO;
                for (AssetSnapshotDetail assetSnapshot : accountList) { //计算总资产 每个币换算成USDT的总和
                    totalBalance = totalBalance.add(assetSnapshot.getBalance().multiply(assetSnapshot.getPrice()));
                }
                //插入一条数据
                snapshots.add(new AssetSnapshot().
                        setUserId(userId).
                        setBalance(totalBalance).
                        setSnapTime(yesterdayDate));
            }
            return this.insertBatch(snapshots);
        }


}
