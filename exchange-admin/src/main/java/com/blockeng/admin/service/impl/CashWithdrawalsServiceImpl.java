package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.*;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.mapper.CashWithdrawalsMapper;
import com.blockeng.admin.service.*;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.AdminUserType;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CashRechargeStatus;
import com.blockeng.framework.enums.CashWithdrawStatus;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 提现表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
@Slf4j
public class CashWithdrawalsServiceImpl extends ServiceImpl<CashWithdrawalsMapper, CashWithdrawals> implements CashWithdrawalsService, Constant {

    @Autowired
    private CashWithdrawAuditRecordService cashRechargeAuditRecord;

    @Autowired
    private UserService userService;

    @Autowired
    private LockService lockService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MultiLevelAuditService multiLevelAuditService;

    @Override
    public Page<UserWithDrawalsDTO> selectMapPage(Page<UserWithDrawalsDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectMapPage(page, paramMap));
    }

    @Override
    public UserWithDrawalsDTO selectOneObj(Long id) {
        return baseMapper.selectOneObj(id);
    }

    /**
     * 法币提现审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    @Override
    @Transactional
    public void cashWithdrawAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException {
        // 校验权限
        if (!multiLevelAuditService.cashWithdrawPermissionCheck(auditDTO.getId(), sysUser)) {
            throw new ExchangeException("审核权限不足");
        }
        boolean isLocked = false;
        try {
            // 通过内存锁防止重复提交审核，导致资金异常
            isLocked = lockService.getLock(REDIS_KEY_CASH_WITHDRAW_AUDIT_LOCK, String.valueOf(auditDTO.getId()), false);
            if (!isLocked) {
                throw new ExchangeException("已经提交审核，请勿重复操作");
            }
            CashWithdrawals cashWithdraw = baseMapper.selectById(auditDTO.getId());
            if (cashWithdraw == null) {
                throw new ExchangeException("提现订单不存在");
            }
            if (cashWithdraw.getStatus() != CashRechargeStatus.PENDING.getCode()) {
                throw new ExchangeException("此记录已审核");
            }
            // 审核轨迹
            CashWithdrawAuditRecord cashWithdrawAuditRecord = new CashWithdrawAuditRecord();
            cashWithdrawAuditRecord.setOrderId(Long.valueOf(auditDTO.getId()))
                    .setStatus(auditDTO.getStatus())
                    .setRemark(auditDTO.getRemark())
                    .setStep(cashWithdraw.getStep())
                    .setAuditUserId(sysUser.getId())
                    .setAuditUserName(sysUser.getFullname());
            cashRechargeAuditRecord.insert(cashWithdrawAuditRecord);
            User user = userService.selectById(cashWithdraw.getUserId());
            cashWithdraw.setRemark(auditDTO.getRemark());
            if (auditDTO.getStatus().intValue() == CashWithdrawStatus.REFUSE.getCode()) {
                // 审核拒绝
                cashWithdraw.setStatus(CashRechargeStatus.REFUSE.getCode());
                cashWithdraw.setLastTime(new Date());
                baseMapper.updateById(cashWithdraw);
                // 更新资金账户(解冻资金账户)
               // accountService.unlockAmount(user.getId(), cashWithdraw.getCoinId(), cashWithdraw.getNum(), BusinessType.WITHDRAW, cashWithdraw.getId());
                accountService.unlockCashAmount(user.getId(), cashWithdraw.getCoinId(), cashWithdraw.getNum(), BusinessType.WITHDRAW, cashWithdraw.getId());

                // 提现审核拒绝短信通知用户
                Map<String, Object> templateParam = new HashMap<>();
                templateParam.put("num", cashWithdraw.getMum());
                templateParam.put("reason", auditDTO.getRemark());

//                SendForm sendForm = new SendForm();
//                sendForm.setCountryCode(user.getCountryCode())
//                        .setMobile(user.getMobile())
//                        .setTemplateCode(SmsTemplate.CASH_WITHDRAW_REFUSE.getCode())
//                        .setEmail(user.getEmail())
//                        .setTemplateParam(templateParam);
//                try{
//                    smsServiceClient.sendTo(sendForm);
//                }catch(Exception e){
//                    e.printStackTrace();
//                    log.error("审核完毕，但是短信发送犯病："+e.getMessage());
//                }
                return;
            }
            // 审核通过
            Config config = configService.queryBuyCodeAndType(CONFIG_TYPE_SYSTEM, Constant.CONFIG_CASH_WITHDRAW_AUDIT_STEPS);
            if (config == null || Strings.isNullOrEmpty(config.getValue())) {
                throw new ExchangeException("没有配置审核级数");
            }
            // 当前审核级数
            int step = cashWithdraw.getStep();
            if (step == Integer.valueOf(config.getValue()).intValue()) {
                // 最终审核通过
                cashWithdraw.setStatus(CashRechargeStatus.SUCCESS.getCode());
                cashWithdraw.setLastTime(new Date());
                baseMapper.updateById(cashWithdraw);
                // 变更账户资金
                this.withAmount(cashWithdraw, user.getId());
                // 法币提现给用户打款后，银行有短信通知，因此平台不在发送短信通知用户
                return;
            }
            // 不是最终审核：状态设置为待审核，审计级别加1级
            cashWithdraw.setStatus(CashRechargeStatus.PENDING.getCode()).setStep(step + 1);
            baseMapper.updateById(cashWithdraw);
            return;
        } catch (AccountException e) {
            throw new ExchangeException(e.getMessage());
        } finally {
            // 释放锁
            if (isLocked) {
                lockService.unlock(REDIS_KEY_CASH_WITHDRAW_AUDIT_LOCK, String.valueOf(auditDTO.getId()));
            }
        }
    }

    /**
     * 提现审核通过修改资金账户
     *
     * @param cashWithdraw 提现申请单
     * @param userId       用户ID
     */
    private boolean withAmount(CashWithdrawals cashWithdraw, Long userId) {
        Account account = accountService.queryByUserIdAndCoinId(userId, cashWithdraw.getCoinId());
        if (account == null) {
            log.error("资金账户异常：userId：{}，coinId：{}", userId, cashWithdraw.getCoinId());
            throw new AccountException("资金账户异常");
        }
        // 解冻资金账户
        accountService.unlockAmount(userId, cashWithdraw.getCoinId(), cashWithdraw.getNum(), BusinessType.WITHDRAW, cashWithdraw.getId());
        User c2cAdmin = userService.queryAdminUser(AdminUserType.C2C_ADMIN);
        if (c2cAdmin == null) {
            log.error("尚未配置C2C管理员用户");
            throw new AccountException("尚未配置C2C管理员用户");
        }
        accountService.transferAmount(userId,
                c2cAdmin.getId(),
                cashWithdraw.getCoinId(),
                cashWithdraw.getNum(),
                BusinessType.WITHDRAW,
                cashWithdraw.getId(),
                BusinessType.WITHDRAW.getDesc());
        return true;
    }


    public Page<CashWithdrawalsCountDTO> selectCountMain(Page<CashWithdrawalsCountDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectCountMain(page, paramMap));
    }

    @Override
    public List<CashWithdrawalsCountDTO> selectValidCounts(Map<String, Object> paramMap) {
        return baseMapper.selectValidCounts(paramMap);
    }

    @Override
    public List<CashWithdrawalsCountDTO> selectUserCt(Map<String, Object> paramMap) {
        return baseMapper.selectUserCt(paramMap);
    }


    @Override
    public CurbExchangeWithdrawStatisticsDTO selectCurbExchangeWithdrawStatistics(int current, int size, String startTime, String endTime, String userId) {

        BigDecimal withdrawAmount=new BigDecimal(0);
        BigDecimal transferAmount=new BigDecimal(0);
        BigDecimal fee=new BigDecimal(0);
        Long withdrawTimes=Long.valueOf(0);
        Long total=Long.valueOf(0);
        CurbExchangeWithdrawStatisticsDTO curbExchangeWithdrawStatisticsDTO=new CurbExchangeWithdrawStatisticsDTO();
        Page<CurbExchangeWithdrawStatistics> page = new Page<>(current, size);

        Map<String, Object> paramMap = new HashMap<>();
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = endTime + " 23:59:59";
//        }

        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("userId",userId);

        //用户明细
        List<CurbExchangeWithdrawStatistics> curbExchangeRechargeStatisticsList=baseMapper.selectCurbExchangeWithdrawStatistics(page,paramMap);
        //总体统计
        Map<String,Object> resultMap=baseMapper.countCurbExchangeWithdraw(paramMap);

        if(curbExchangeRechargeStatisticsList!=null && curbExchangeRechargeStatisticsList.size()>0){
            withdrawAmount=(BigDecimal)resultMap.get("withdrawAmount");
            transferAmount=(BigDecimal)resultMap.get("transferAmount");
            fee=(BigDecimal)resultMap.get("fee");
            withdrawTimes=(Long)resultMap.get("withdrawTimes");
            total=(Long)resultMap.get("total");
            curbExchangeWithdrawStatisticsDTO.setRecords(curbExchangeRechargeStatisticsList);
        }
        curbExchangeWithdrawStatisticsDTO.setFee(fee);
        curbExchangeWithdrawStatisticsDTO.setWithdrawAmount(withdrawAmount);
        curbExchangeWithdrawStatisticsDTO.setTransferAmount(transferAmount);
        curbExchangeWithdrawStatisticsDTO.setWithdrawTimes(withdrawTimes.intValue());
        curbExchangeWithdrawStatisticsDTO.setCurrent(current).setSize(size).setTotal(total.intValue());

        return curbExchangeWithdrawStatisticsDTO;
    }

    @Override
    public List<Map<String, Object>> selectCashWithdrawGroupCoin(String userId) {
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",3);
        return baseMapper.selectCashWithdrawGroupCoin(paramMap);
    }


    @Override
    public List<Map<String, Object>> selectCashWithdrawFreezeByUserGroupCoin(String userId) {
        //冻结时的状态 [0.待审核|1.审核通过] 先这么写一下
        String freezeStatus="0,1";
        Map<String,Object> paramMap=new HashMap<String,Object>();
        paramMap.put("userId",userId);
        paramMap.put("status",freezeStatus);
        return baseMapper.selectCashWithdrawFreezeGroupByCoin(paramMap);
    }
}
