package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.AuditDTO;
import com.blockeng.admin.dto.BuckleAccountCountDTO;
import com.blockeng.admin.dto.CoinBuckleDTO;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.mapper.CoinBuckleMapper;
import com.blockeng.admin.service.*;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CoinBuckleStatus;
import com.blockeng.framework.enums.CoinWithdrawStatus;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blockeng.framework.constants.Constant.*;

@Slf4j
@Service
public class CoinBuckleServiceImpl extends ServiceImpl<CoinBuckleMapper, CoinBuckle> implements CoinBuckleService {

    @Autowired
    CoinBuckleAuditRecordService coinBuckleAuditRecordService;

    @Autowired
    MultiLevelAuditService multiLevelAuditService;

    @Autowired
    LockService lockService;

    @Autowired
    UserService userService;

    @Autowired
    ConfigService configService;

    @Autowired
    CoinConfigService coinConfigService;

    @Autowired
    AccountService accountService;

    @Override
    public List<CoinBuckleDTO> selectListPage(Page<CoinBuckleDTO> page, EntityWrapper<CoinBuckleDTO> ew) {
        return baseMapper.selectDTOList(page.getSize(), page.getCurrent(), ew);
    }

    @Override
    @Transactional
    public boolean audit(AuditDTO auditDTO, SysUser sysUser) {

        log.info("coinWithdrawAudit auditDTO:" + auditDTO);
        // 校验权限
        if (!multiLevelAuditService.coinBucklePermissionCheck(auditDTO.getId(), sysUser)) {
            throw new ExchangeException("审核权限不足");
        }
        boolean isLocked = false;
        try {
            // 通过内存锁防止重复提交审核，导致资金异常
            isLocked = lockService.getLock(REDIS_KEY_CASH_BUCKLE_AUDIT_LOCK, String.valueOf(auditDTO.getId()), false);
            if (!isLocked) {
                throw new ExchangeException("已经提交审核，请勿重复操作");
            }
            CoinBuckle coinBuckle = baseMapper.selectById(auditDTO.getId());
            if (coinBuckle == null) {
                throw new ExchangeException("提现订单不存在");
            }
            if (coinBuckle.getStatus() != CoinBuckleStatus.PENDING.getCode()) { //CoinWithdrawStatus.PENDING.getCode()) {
                throw new ExchangeException("此记录已审核");
            }
            CoinBuckleAuditRecord coinBuckleAuditRecord = new CoinBuckleAuditRecord();
            coinBuckleAuditRecord.setOrderId(auditDTO.getId())
                    .setStatus(auditDTO.getStatus())
                    .setRemark(auditDTO.getRemark())
                    .setStep(coinBuckle.getStep())
                    .setAuditUserId(sysUser.getId())
                    .setAuditUserName(sysUser.getFullname());
            coinBuckleAuditRecordService.insert(coinBuckleAuditRecord);
            coinBuckle.setRemark(auditDTO.getRemark());
            if (auditDTO.getStatus().intValue() == CoinWithdrawStatus.REFUSE.getCode()) {
                coinBuckle.setStatus(CoinBuckleStatus.REFUSE.getCode())
                        .setAuditTime(new Date());
                baseMapper.updateById(coinBuckle); //更新状态
                if (coinBuckle.getType() == 2) {
                    accountService.unlockAmount(coinBuckle.getAccountId(), //解冻
                            coinBuckle.getAmount(),
                            BusinessType.BUCKLE_UNLOCK,
                            coinBuckle.getId());
                }
                return true;
            }
            // 审核通过
            Config config = configService.queryBuyCodeAndType(CONFIG_TYPE_SYSTEM, CONFIG_COIN_BUCKLE_AUDIT_STEPS);
            if (null == config || Strings.isNullOrEmpty(config.getValue())) {
                throw new ExchangeException("没有配置审核级数");
            }
            int step = coinBuckle.getStep();
            if (step == Integer.valueOf(config.getValue()).intValue()) {
                Account account = accountService.selectById(coinBuckle.getAccountId());
                if (null == account) {
                    throw new ExchangeException("资金账户不存在");
                }
                coinBuckle.setStatus(CoinBuckleStatus.SUCCESS.getCode());
                coinBuckle.setAuditTime(new Date());
                baseMapper.updateById(coinBuckle);
                if (coinBuckle.getType() == 1) {
                    accountService.addAmount(coinBuckle.getAccountId(), coinBuckle.getAmount(), BusinessType.BUCKLE, BusinessType.BUCKLE.getDesc(), coinBuckle.getId());
                }
                if (coinBuckle.getType() == 2) {
                    accountService.unlockAmount(coinBuckle.getAccountId(), //解冻
                            coinBuckle.getAmount(),
                            BusinessType.BUCKLE_UNLOCK,
                            coinBuckle.getId());
                    accountService.subtractAmount(coinBuckle.getAccountId(), BigDecimal.ZERO, //扣款
                            coinBuckle.getAmount(), BusinessType.BUCKLE,
                            BusinessType.BUCKLE.getDesc(), coinBuckle.getId());
                }
                return true;
            }

            coinBuckle.setStatus(CoinBuckleStatus.PENDING.getCode()).setStep(step + 1);
            baseMapper.updateById(coinBuckle);
        } catch (AccountException e) {
            throw new ExchangeException(e.getMessage());
        } finally {
            // 释放锁
            if (isLocked) {
                lockService.unlock(REDIS_KEY_CASH_BUCKLE_AUDIT_LOCK, String.valueOf(auditDTO.getId()));
            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean addCoinBuckle(CoinBuckle coinBuckle) {
        Account account = null;
        if (null != coinBuckle.getAccountId()) {
            account = accountService.selectById(coinBuckle.getAccountId());
            if (null == account) {
                throw new ExchangeException("资金账户不存在");
            }
        } else if (null != coinBuckle.getUserId() && null != coinBuckle.getCoinId()) {
            account = accountService.queryByUserIdAndCoinId(coinBuckle.getUserId(), coinBuckle.getCoinId());
            if (null == account) {
                throw new ExchangeException("资金账户不存在或不是真实用户");
            }
            coinBuckle.setAccountId(account.getId());
        } else {
            return false;
        }
        coinBuckle.setStep(1);
        coinBuckle.setStatus(CoinBuckleStatus.PENDING.getCode());
        if (coinBuckle.getType() == 1) { //补 补钱不需要冻结啥
            insert(coinBuckle);
        } else if (coinBuckle.getType() == 2) {//扣
            // 冻结,修改account，增加accountDetail，增加扣款信息
            if (accountService.lockAmount(coinBuckle.getAccountId(), coinBuckle.getAmount(), coinBuckle.getId(), BusinessType.BUCKLE_LOCK)) {
                insert(coinBuckle);
            }
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> selectBuckleFreezeByCoin(String userId) {
        Map<String,Object> paramMap=new HashMap<String, Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",0);
        paramMap.put("type",2);
        return baseMapper.selectCoinBuckleFreezeGroupCoin(paramMap);
    }

    @Override
    public List<Map<String, Object>> selectCoinBuckleGroupCoin(String userId, Integer type) {
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("type",type);
        paramMap.put("status",3);
        return baseMapper.selectCoinBuckleGroupCoin(paramMap);
    }

    @Override
    public BigDecimal selectSumTotal(EntityWrapper<BuckleAccountCountDTO> ew) {
        return baseMapper.selectSumTotal(ew);
    }


    @Override
    public BigDecimal selectSubTotal(EntityWrapper<BuckleAccountCountDTO> ew) {
        return baseMapper.selectSubTotal(ew);
    }

    @Override
    public List<BuckleAccountCountDTO> selectBuckleAccountCounts(Integer current, Integer size, EntityWrapper<BuckleAccountCountDTO> ew) {
        return baseMapper.selectBuckleAccountCounts(current, size, ew);
    }

    @Override
    public int selectListPageCount(Page<CoinBuckleDTO> page, EntityWrapper<CoinBuckleDTO> ew) {
        return baseMapper.selectListPageCount(ew);
    }

    @Override
    public int selectBuckleAccountCountsTotal(EntityWrapper<BuckleAccountCountDTO> ew) {
        return baseMapper.selectBuckleAccountCountsTotal(ew);
    }


}
