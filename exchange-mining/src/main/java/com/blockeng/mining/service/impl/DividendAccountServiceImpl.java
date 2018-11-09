package com.blockeng.mining.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.TradeMarketDTO;
import com.blockeng.framework.dto.UnlockDTO;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.dto.DividendAccountDTO;
import com.blockeng.mining.entity.*;
import com.blockeng.mining.mapper.DividendAccountMapper;
import com.blockeng.mining.mapper.DividendReleaseRecordMapper;
import com.blockeng.mining.service.*;
import com.blockeng.mining.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional
public class DividendAccountServiceImpl extends ServiceImpl<DividendAccountMapper, DividendAccount> implements DividendAccountService {


    @Autowired
    private MineHelpService mineHelpService;


    @Autowired
    private DividendRecordService dividendRecordService;

    @Autowired
    private MineService mineService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private DividendReleaseRecordMapper dividendReleaseRecordMapper;



    @Override
    public Page<DividendAccountDTO> dividendAccountUnlockList(Page<DividendReleaseRecord> page, Long id) {

        TradeMarketDTO currentMarket = mineHelpService.getMineCurrentMarket();
        QueryWrapper<DividendReleaseRecord> ew = new QueryWrapper<>();
        ew.eq("user_id", id);
        IPage<DividendReleaseRecord> dividendReleaseRecordIPage = this.dividendReleaseRecordMapper.selectPage(page, ew);
        List<DividendReleaseRecord> records = dividendReleaseRecordIPage.getRecords();
        List<DividendAccountDTO> dividendAccountDTOList = new ArrayList<>(records.size());
        if (!CollectionUtils.isEmpty(records)) {
            for (DividendReleaseRecord item : records) {
                DividendAccountDTO dividendAccountDTO = new DividendAccountDTO();
                dividendAccountDTO.setRewardAmount(item.getWeekLock()).
                        setLockAmount(item.getWeekLock().subtract(item.getUnlockAmount())).
                        setUnlockAmount(item.getUnlockAmount()).
                        setUnlockDate(item.getUnlockDate()).
                        setCnyAccount(item.getWeekLock().multiply(currentMarket.getCnyPrice())).
                        setUsdtAccount(item.getWeekLock().multiply(currentMarket.getPrice()));
                dividendAccountDTOList.add(dividendAccountDTO);
            }
        }



        Page<DividendAccountDTO> dividendAccountDTOPage = new Page<>();
        dividendAccountDTOPage.
                setRecords(dividendAccountDTOList).
                setCurrent(dividendReleaseRecordIPage.getCurrent()).
                setSize(dividendReleaseRecordIPage.getSize()).
                setTotal(dividendReleaseRecordIPage.getTotal());
        return dividendAccountDTOPage;

    }


    @Override
    public Object unlockdividendAccount(Long id, BigDecimal lockAmount) {

        BigDecimal totalUnlock = lockAmount;

        BigDecimal priWeekTotalMine = mineService.priWeekTotalMine(TimeUtils.getNowDay(), id);
        BigDecimal priWeekTotal = dividendRecordService.selectTotalPriWeek(id);

        if (priWeekTotalMine == null || priWeekTotalMine.compareTo(BigDecimal.ZERO) <= 0) {
            log.info("上周您未参与交易,无法解冻");
              return Response.err(1000017, "上周您未参与交易,无法解冻");
        } else {
            DividendReleaseRecord dividendReleaseRecord = new DividendReleaseRecord();
            dividendReleaseRecord.setWeekLock(priWeekTotal);
            if (totalUnlock.compareTo(priWeekTotalMine) > 0) totalUnlock = priWeekTotalMine;
            dividendReleaseRecord
                    .setPriWeekTotalMine(priWeekTotalMine)
                    .setUserId(id)
                    .setUnlockAmount(totalUnlock)
                    .setUnlockDate(TimeUtils.getNowDay());
            if (dividendReleaseRecordMapper.insert(dividendReleaseRecord) > 0) {
                DividendAccount dividendAccount = new DividendAccount()
                        .setLastUpdateTime(new Date())
                        .setUnlockAmount(totalUnlock)
                        .setUnlockDate(TimeUtils.getNowDay())
                        .setUserId(id);
                if (this.baseMapper.updateByUserID(dividendAccount) > 0) {
                    UnlockDTO unlockDTO = new UnlockDTO().
                            setAmount(dividendAccount.getUnlockAmount()).
                            setCoinId(mineHelpService.getMineCoinId()).
                            setBusinessType(BusinessType.DIVIDEND_DIG).
                            setDesc(BusinessType.DIVIDEND_DIG.getDesc()).
                            setUserId(dividendAccount.getUserId()).
                            setOrderId(dividendReleaseRecord.getId());
                    log.info("----unlockDTO-----"+ GsonUtil.toJson(unlockDTO));
                    log.info("----dividendReleaseRecord.getId()-----"+ dividendReleaseRecord.getId());
//                    log.info("----dividendAccount.getId()-----"+ dividendAccount.getId());
                     rabbitTemplate.convertAndSend("pool.unlock", GsonUtil.toJson(unlockDTO));
                    return Response.ok(unlockDTO);
                }


            }
        }
        return Response.err(1000018, "服务器异常请联系客服");
    }

    @Override
    public Object dividendAccountTotal(Long id) {
        QueryWrapper<DividendAccount> ew = new QueryWrapper<>();
        ew.eq("user_id", id);
        DividendAccount dividendAccount = this.baseMapper.selectOne(ew);
        DividendAccountDTO dividendAccountDTO = new DividendAccountDTO();
        if (null != dividendAccount) {
            TradeMarketDTO mineCurrentMarket = mineHelpService.getMineCurrentMarket();
            BigDecimal totalRewardAmount = dividendAccount.getRewardAmount();
            dividendAccountDTO
                    .setLockAmount(dividendAccount.getLockAmount())
                    .setUnlockAmount(dividendAccount.getUnlockAmount())
                    .setUnlockDate(dividendAccount.getUnlockDate())
                    .setRewardAmount(totalRewardAmount)
                    .setUsdtAccount(totalRewardAmount.multiply(mineCurrentMarket.getPrice()))
                    .setCnyAccount(totalRewardAmount.multiply(mineCurrentMarket.getCnyPrice()));
        }
        return Response.ok(dividendAccountDTO);
    }
}
