package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.dto.NowWeekDividendDTO;
import com.blockeng.mining.dto.PoolDividendRecordDTO;
import com.blockeng.mining.dto.PoolDividendTotalAccountDTO;
import com.blockeng.mining.dto.PoolUserHoldCoinDTO;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.mapper.PoolDividendRecordMapper;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class PoolDividendRecordServiceImpl extends ServiceImpl<PoolDividendRecordMapper, PoolDividendRecord> implements PoolDividendRecordService {

    @Autowired
    private UserService userService;

    @Autowired
    private MinePoolService minePoolService;


    @Autowired
    private MineService mineService;


    @Autowired
    private ConfigServiceClient configServiceClient;

    @Autowired
    private MineHelpService mineHelpService;

    @Autowired
    private PlantCoinDividendRecordService plantCoinDividendRecordService;

    @Autowired
    private AssetSnapshotDetailService assetSnapshotDetailService;

    @Autowired
    private PoolDividendAccountService poolDividendAccountService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void dividend() {
        Long minePollSize = Long.valueOf(configServiceClient.getConfig("Mining", "MINE_POLL_SIZE").getValue());//矿池成员数量条件
        Long coinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue());//挖矿币种ID
        String minePollRate = configServiceClient.getConfig("Mining", "MINE_POLL_RATE").getValue();//矿池奖励条件比例
        String minePollRewardRate = configServiceClient.getConfig("Mining", "MINE_POLL_REWARD_RATE").getValue();//矿池奖励比例

        if (coinId <= 0 || StringUtils.isEmpty(minePollRate) || StringUtils.isEmpty(minePollRewardRate)) {
            log.error("计算矿主收益参数错误");
            return;
        }

        String yesterdayDate = TimeUtils.getYesterdayDate();

        //查询所有符合要求的矿池
        QueryWrapper<MinePool> ew = new QueryWrapper<>();
        ew.eq("status", 1);//审核通过的矿池
        List<MinePool> minePools = minePoolService.selectList(ew);
        if (CollectionUtils.isEmpty(minePools)) {
            log.error("不存在有效矿池");
            return;
        }

        BigDecimal dayTotalMine = mineService.dayTotalMine(yesterdayDate);
        if (null == dayTotalMine) {
            log.error("前一天不存在任何挖矿信息");
            return;
        }
        //矿主收益
        BigDecimal poolIncome = dayTotalMine.multiply(new BigDecimal(minePollRewardRate)).divide(BigDecimal.valueOf(minePools.size()), 4, RoundingMode.DOWN);


        //平均持有量 矿池持币量大于平均持有量，计算公式为：总流通量*设定百分比/矿主数
        BigDecimal avgDividend = plantCoinDividendRecordService.totalDividend().
                multiply(new BigDecimal(minePollRate)).
                divide(new BigDecimal(minePools.size()),4,RoundingMode.HALF_UP);

        /**
         * 获取平台前一天的币种持有量
         */

        for (MinePool item : minePools) {
            Long createUser = item.getCreateUser();//获取矿池创建人

            List<User> allPoolMember = getPoolAllUser(createUser);//获取当前矿池所有用户
            BigDecimal poolHoldCoin = getTotalUserHold(createUser, allPoolMember);

            if (poolHoldCoin.compareTo(avgDividend) <= 0) {
                log.info("矿主ID---"+createUser+"平均持币量为" + avgDividend.toString() + ",当前矿主持有的币不够" + poolHoldCoin);
                savePoolDividend(createUser,
                        "平均持币量为" + avgDividend.toString() + ",当前矿主持有的币不够" + poolHoldCoin,
                        BigDecimal.ZERO);
                continue;
            }


            if (!CollectionUtils.isEmpty(allPoolMember) && allPoolMember.size() >= minePollSize) {//如果result不为空,循环获取挖矿信息
                log.info("矿主ID:"+createUser+",平均持币量为:" + avgDividend.toString() + ",当前矿主持有的币:" + poolHoldCoin,
                        ",success",",矿主收益:"+poolIncome);
                savePoolDividend(createUser, "success", poolIncome);
            } else {
                log.info("矿池最低合格人数为:" + minePollSize + ",当前矿池成员数为:" + (null == allPoolMember ? "0" : "" + allPoolMember.size()));
                savePoolDividend(createUser, "矿池最低合格人数为:" + minePollSize + ",当前矿池成员数为:" + (null == allPoolMember ? "0" : "" + allPoolMember.size()), BigDecimal.ZERO);
            }

        }
    }

    private Integer savePoolDividend(Long createUser, String mark, BigDecimal amount) {
        return this.baseMapper.insert(new PoolDividendRecord().
                setAmount(amount).
                setRewardDate(TimeUtils.getYesterdayDate()).
                setUserId(createUser).
                setMark(mark));
    }

    /**
     * 获取当前矿池所有合格的用户
     *
     * @param createUser
     * @return
     */

    public List<User> getPoolAllUser(Long createUser) {
        //查询出所有已经实名认证,并且有填写了邀请码的用户
        List<User> userList = userService.inviteList();

        Map<Long, List<User>> userMap = userList.stream().collect(Collectors.groupingBy(User::getDirectInviteid));

        List<User> allPoolMember = new ArrayList<>();
        allPoolMember=getChild(allPoolMember, userMap.get(createUser), userMap);
        log.info("---allPoolMember---"+allPoolMember.size());
        return allPoolMember;
    }

    @Override
    public Object poolRecordListDetail(Page<PoolDividendRecord> page, String startTime, String endTime, Long id) {
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        IPage<PoolDividendRecord> poolDividendRecordIPage = this.baseMapper.selectPage(page,
                new QueryWrapper<PoolDividendRecord>().eq("user_id", id).between("reward_date", startTime, endTime));
        Page<PoolDividendRecordDTO> poolDividendRecordDTOPage = new Page<>();
        poolDividendRecordDTOPage.setCurrent(poolDividendRecordIPage.getCurrent()).
                setTotal(poolDividendRecordIPage.getTotal()).setSize(poolDividendRecordIPage.getSize()).setRecords(new ArrayList<>());
        if (!CollectionUtils.isEmpty(poolDividendRecordIPage.getRecords())) {
            for (PoolDividendRecord item : poolDividendRecordIPage.getRecords()) {
                poolDividendRecordDTOPage.getRecords().add(new PoolDividendRecordDTO().setAmount(item.getAmount()).
                        setRewardDate(item.getRewardDate()).
                        setCnyAccount(item.getAmount().multiply(mineCurrentMarket.getCnyPrice())).
                        setUsdtAccount(item.getAmount().multiply(mineCurrentMarket.getPrice())));
            }
        }

        return poolDividendRecordDTOPage;
    }

    @Override
    public Object poolUnAccount(Long id) {
        MinePool poolUser = minePoolService.getPoolUser(id);
        if (null == poolUser) {
            return Response.err(1000011, "当前用户不是矿主");
        }
        QueryWrapper<PoolDividendRecord> qw = new QueryWrapper<>();
        qw.between("reward_date", TimeUtils.getPriMonethFirst(TimeUtils.getNowDay()), TimeUtils.getPriMonethLast(TimeUtils.getNowDay()));
        qw.eq("user_id", id);
        BigDecimal totalUnlock = this.baseMapper.selectTotalUnAcount(qw);
        //todo 需要累计
        PoolDividendTotalAccountDTO poolDividendTotalAccountDTO = poolDividendAccountService.selectPriTotalUnAcount(id);
        BigDecimal priMonthTotalMine = mineService.priMonthTotalMine(TimeUtils.getNowDay(), id);

        if (priMonthTotalMine==null||priMonthTotalMine.compareTo(BigDecimal.ZERO) <= 0) {
            return Response.err(1000012, "上月交易挖矿总量为0,解冻失败");
        } else {
            PoolDividendAccount poolDividendAccount = new PoolDividendAccount();


            BigDecimal totalUnlockAndB4 = totalUnlock.add(poolDividendTotalAccountDTO.getTotalLockAmount());
            BigDecimal priMonthUnLockAmount = priMonthTotalMine.compareTo(totalUnlockAndB4) >= 0 ? totalUnlockAndB4 : priMonthTotalMine;
            poolDividendAccount.setRewardAmount(totalUnlock)
                                .setPriMonthTotalMine(priMonthTotalMine)
                                .setLockAmount(totalUnlock.subtract(priMonthUnLockAmount))
                                .setUnlockAmount(priMonthUnLockAmount)
                                .setUserId(id)
                                .setUnlockDate(TimeUtils.getNowDay());
            poolDividendAccountService.insert(poolDividendAccount);
            UnlockDTO unlockDTO = new UnlockDTO().
                    setAmount(poolDividendAccount.getUnlockAmount()).
                    setCoinId(mineHelpService.getMineCoinId()).
                    setBusinessType(BusinessType.POOL_DIG).
                    setDesc(BusinessType.POOL_DIG.getDesc()).
                    setUserId(poolDividendAccount.getUserId()).
                    setOrderId(poolDividendAccount.getId());
            rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));
            return Response.ok(unlockDTO);
        }
    }

    @Override
    public Object dividendAccountThisMonth(Long userId) {
        QueryWrapper<PoolDividendRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", userId);
        ew.ge("reward_date", TimeUtils.getMonethFirst(null));
        BigDecimal totalAmount = this.baseMapper.selectTotalUnAcount(ew);
        if (null == totalAmount || totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new NowWeekDividendDTO();
        }
        TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
        return new NowWeekDividendDTO().setTotalAmount(totalAmount).
                setUsdtAmount(totalAmount.multiply(mineCurrentMarket.getPrice())).
                setCnyAmount(totalAmount.multiply(mineCurrentMarket.getCnyPrice()));
    }


    /**
     * 根据矿池用户获取当前用户持币量
     */
    public List<PoolUserHoldCoinDTO> getPoolUserHold(Long createUser, List<User> allPoolMember) {
        MinePool poolUser = minePoolService.getPoolUser(createUser);
        ArrayList<PoolUserHoldCoinDTO> userHoldList = new ArrayList<>();
        if (null == poolUser) {
            log.error("当前用户不是矿主");
            return new ArrayList<>();
        }
        //List<User> allPoolMember = getPoolAllUser(createUser);//获取当前矿池的所有合格用户
        if (!CollectionUtils.isEmpty(allPoolMember)) {
            QueryWrapper<AssetSnapshotDetail> ewAsset = new QueryWrapper<AssetSnapshotDetail>().eq("snap_time", TimeUtils.getYesterdayDate()).eq("coin_id", mineHelpService.getMineCoinId());
            List<AssetSnapshotDetail> assetSnapshotDetails = assetSnapshotDetailService.selectList(ewAsset);
            if (!CollectionUtils.isEmpty(assetSnapshotDetails)) {
                Map<Long, AssetSnapshotDetail> assetSnapshotDetailMap = new HashMap<>(assetSnapshotDetails.size());
                for (AssetSnapshotDetail assetSnapshotDetail : assetSnapshotDetails) {
                    assetSnapshotDetailMap.put(assetSnapshotDetail.getUserId(), assetSnapshotDetail);
                }
                for (User itemUser : allPoolMember) {
                    AssetSnapshotDetail assetSnapshotDetail = assetSnapshotDetailMap.get(itemUser.getId());
                    if (assetSnapshotDetail != null)
                    userHoldList.add(new PoolUserHoldCoinDTO().setUserName(itemUser.getUsername()).setEmail(itemUser.getEmail()).setMobile(itemUser.getMobile()).
                            setAmount(assetSnapshotDetail.getFreezeAmount().add(assetSnapshotDetail.getBalanceAmount())));
                }
                AssetSnapshotDetail assetSnapshotDetail = assetSnapshotDetailMap.get(createUser);
                if (null != assetSnapshotDetail) {
                    User user = userService.selectById(createUser);
                    userHoldList.add(new PoolUserHoldCoinDTO().setUserName(user.getUsername()).setEmail(user.getEmail()).setMobile(user.getMobile()).
                            setAmount(assetSnapshotDetail.getFreezeAmount().add(assetSnapshotDetail.getBalanceAmount())));
                }
            }

        }
        return userHoldList;
    }


    /**
     * 获取当前矿池所有持币量
     */
    public BigDecimal getTotalUserHold(Long createUser, List<User> allPoolMember) {
        List<PoolUserHoldCoinDTO> poolUserHold = getPoolUserHold(createUser, allPoolMember);
        BigDecimal totalHold = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(poolUserHold)) {
            for (PoolUserHoldCoinDTO item : poolUserHold) {
                totalHold = totalHold.add(item.getAmount());
            }
        }
        return totalHold;
    }


    /**
     * 递归遍历所有子节点
     *
     * @param result  用于存储数据返回数据
     * @param list    需要递归循环的数据
     * @param userMap 原数据
     * @return 返回素有符合条件的孩子
     */
    public List<User> getChild(List<User> result, List<User> list, Map<Long, List<User>> userMap) {
        if (result==null||list==null||list.size()==0||userMap==null||userMap.size()==0) return result;
        for (int i = 0; i < list.size(); i++) {
            User user = list.get(i);
            result.add(user);
            List<User> list1 = userMap.get(user.getId());
            if (!CollectionUtils.isEmpty(list1)) {
                getChild(result, list1, userMap);
            }
        }
        return result;
    }
}
