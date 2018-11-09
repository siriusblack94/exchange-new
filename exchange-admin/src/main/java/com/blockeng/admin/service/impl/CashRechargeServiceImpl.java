package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.dto.*;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.mapper.CashRechargeMapper;
import com.blockeng.admin.service.*;
import com.blockeng.feign.RewardServiceClient;
import com.blockeng.feign.SmsServiceClient;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.AdminUserType;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CashRechargeStatus;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.ExchangeException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 充值表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
@Slf4j
public class CashRechargeServiceImpl extends ServiceImpl<CashRechargeMapper, CashRecharge> implements CashRechargeService, Constant {

    @Autowired
    private CashRechargeAuditRecordService cashRechargeAuditRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private SmsServiceClient smsServiceClient;

    @Autowired
    private LockService lockService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MultiLevelAuditService multiLevelAuditService;

    @Autowired
    private RewardServiceClient rewardServiceClient;

    public Page<UserCashRechargeDTO> selectMapPage(Page<UserCashRechargeDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectMapPage(page, paramMap));
    }

    public UserCashRechargeDTO selectOneObj(Long id) {
        return baseMapper.selectOneObj(id);
    }

    @Override
    public List<UserCashRechargeDTO> selectUserCashRechargeDTOList(Integer pageSize) {
        return baseMapper.selectUserCashRechargeDTOList(pageSize);
    }

    /**
     * 法币充值审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @return
     */
    @Override
    @Transactional
    public void cashRechargeAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException {
        // 校验权限
        if (!multiLevelAuditService.cashRechargePermissionCheck(auditDTO.getId(), sysUser)) {
            throw new ExchangeException("审核权限不足");
        }
        boolean isLocked = false;
        try {
            // 通过内存锁防止重复提交审核，导致资金异常
            isLocked = lockService.getLock(REDIS_KEY_CASH_RECHARGE_AUDIT_LOCK, String.valueOf(auditDTO.getId()), false);
            if (!isLocked) {
                throw new ExchangeException("已经提交审核，请勿重复操作");
            }
            CashRecharge cashRecharge = baseMapper.selectById(auditDTO.getId());
            if (cashRecharge == null) {
                throw new ExchangeException("充值订单不存在");
            }
            if (cashRecharge.getStatus() != CashRechargeStatus.PENDING.getCode()) {
                throw new ExchangeException("此记录已审核");
            }
            // 审核轨迹
            CashRechargeAuditRecord cashRechargeAuditRecord = new CashRechargeAuditRecord();
            cashRechargeAuditRecord.setOrderId(Long.valueOf(auditDTO.getId()))
                    .setStatus(auditDTO.getStatus())
                    .setRemark(auditDTO.getRemark())
                    .setStep(cashRecharge.getStep())
                    .setAuditUserId(sysUser.getId())
                    .setAuditUserName(sysUser.getFullname());
            cashRechargeAuditRecordService.insert(cashRechargeAuditRecord);
            User user = userService.selectById(cashRecharge.getUserId());
            // 更新提现申请单状态
            cashRecharge.setAuditRemark(auditDTO.getRemark());
            if (auditDTO.getStatus().intValue() == CashRechargeStatus.REFUSE.getCode()) {
                // 审核拒绝
                cashRecharge.setStatus(CashRechargeStatus.REFUSE.getCode());
                cashRecharge.setLastTime(new Date());
                cashRecharge.setAuditRemark(auditDTO.getRemark());
                baseMapper.updateById(cashRecharge);
                Map<String, Object> templateParam = new HashMap<>();
                templateParam.put("num", cashRecharge.getMum());
                templateParam.put("reason", auditDTO.getRemark());
//                SendForm sendForm = new SendForm();
//                sendForm.setCountryCode(user.getCountryCode())
//                        .setMobile(user.getMobile())
//                        .setTemplateCode(SmsTemplate.UNDER_LINE_REFUSE.getCode())
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
            Config config = configService.queryBuyCodeAndType(CONFIG_TYPE_SYSTEM, CONFIG_CASH_RECHARGE_AUDIT_STEPS);
            if (config == null || Strings.isNullOrEmpty(config.getValue())) {
                throw new ExchangeException("没有配置审核级数");
            }
            int step = cashRecharge.getStep();
            if (step == Integer.valueOf(config.getValue()).intValue()) {
                // 最终审核通过
                cashRecharge.setStatus(CashRechargeStatus.SUCCESS.getCode());
                cashRecharge.setLastTime(new Date());
                cashRecharge.setAuditRemark(auditDTO.getRemark());
                baseMapper.updateById(cashRecharge);
                // 变更账户资金
                this.rechargeAmount(cashRecharge, user.getId());
                //首次充值奖励
                rewardServiceClient.inviteReward(user.getId());
                // 短信通知用户
                Map<String, Object> templateParam = new HashMap<>();
                templateParam.put("num", cashRecharge.getMum());
                templateParam.put("getTime", DateUtils.formatDate(new DateTime().toDate(), "yyyy-MM-dd HH:mm:ss"));
//                SendForm sendForm = new SendForm();
//                sendForm.setCountryCode(user.getCountryCode())
//                        .setMobile(user.getMobile())
//                        .setTemplateCode(SmsTemplate.UNDER_LINE_SUCCESS.getCode())
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
            // 不是最终审核，审计级别加一级，状态为待审核
            cashRecharge.setStatus(CashRechargeStatus.PENDING.getCode()).setStep(step + 1);
            baseMapper.updateById(cashRecharge);
            return;
        } catch (AccountException e) {
            throw new ExchangeException(e.getMessage());
        } finally {
            // 释放锁
            if (isLocked) {
                lockService.unlock(REDIS_KEY_CASH_RECHARGE_AUDIT_LOCK, String.valueOf(auditDTO.getId()));
            }
        }
    }

    public Page<CashRechargeCountDTO> selectCountMain(Page<CashRechargeCountDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectCountMain(page, paramMap));
    }

    @Override
    public List<CashRechargeCountDTO> selectValidCounts(Map<String, Object> paramMap) {
        return baseMapper.selectValidCounts(paramMap);
    }

    @Override
    public List<CashRechargeCountDTO> selectUserCt(Map<String, Object> paramMap) {
        return baseMapper.selectUserCt(paramMap);
    }

    /**
     * 人民币充值
     *
     * @param cashRecharge 充值申请单
     * @param userId       用户ID
     * @return
     */
    private boolean rechargeAmount(CashRecharge cashRecharge, long userId) throws AccountException {
        Account account = accountService.queryByUserIdAndCoinId(userId, cashRecharge.getCoinId());
        if (account == null) {
            log.error("资金账户异常：userId：{}，coinId：{}", userId, cashRecharge.getCoinId());
            throw new AccountException("资金账户异常");
        }
        User c2cAdmin = userService.queryAdminUser(AdminUserType.C2C_ADMIN);
        if (c2cAdmin == null) {
            log.error("尚未配置C2C管理员用户");
            throw new AccountException("尚未配置C2C管理员用户");
        }
        accountService.transferAmount(c2cAdmin.getId(),
                userId,
                cashRecharge.getCoinId(),
                cashRecharge.getNum(),
                BusinessType.RECHARGE,
                cashRecharge.getId(),
                BusinessType.RECHARGE.getDesc());
        return true;
    }


    @Override
    public CurbExchangeRechargeStatisticsDTO selectCurbExchangeRechargeStatistics(int current, int size, String startTime, String endTime, String userId) {

        BigDecimal rechargeAmount = new BigDecimal(0);
        BigDecimal transferAmount = new BigDecimal(0);
        Long rechargeTimes = Long.valueOf(0);
        Long total = Long.valueOf(0);
        CurbExchangeRechargeStatisticsDTO curbExchangeRechargeStatisticsDTO = new CurbExchangeRechargeStatisticsDTO();
        Page<CurbExchangeRechargeStatistics> page = new Page<>(current, size);

        Map<String, Object> paramMap = new HashMap<>();
//        if (StringUtils.isNotBlank(endTime)) {
//            endTime = endTime + " 23:59:59";
//        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }
        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("userId", userId);

        //用户明细
        List<CurbExchangeRechargeStatistics> curbExchangeRechargeStatisticsList = baseMapper.selectCurbExchangeRechargeStatistics(page, paramMap);
        //总体统计
        Map<String, Object> resultMap = baseMapper.countCurbExchangeRecharge(paramMap);

        if (curbExchangeRechargeStatisticsList != null && curbExchangeRechargeStatisticsList.size() > 0) {

            rechargeAmount = (BigDecimal) resultMap.get("rechargeAmount");
            transferAmount = (BigDecimal) resultMap.get("transferAmount");
            rechargeTimes = (Long) resultMap.get("rechargeTimes");
            total = (Long) resultMap.get("total");
            curbExchangeRechargeStatisticsDTO.setRecords(curbExchangeRechargeStatisticsList);
        }

        curbExchangeRechargeStatisticsDTO.setRechageAmount(rechargeAmount);
        curbExchangeRechargeStatisticsDTO.setTransferAmount(transferAmount);
        curbExchangeRechargeStatisticsDTO.setRechargeTimes(rechargeTimes.intValue());
        curbExchangeRechargeStatisticsDTO.setCurrent(current).setSize(size).setTotal(total.intValue());
        return curbExchangeRechargeStatisticsDTO;
    }

    @Override
    public List<Map<String, Object>> selectCashRechargeByUserGroupCoin(String userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", 3);
        return baseMapper.selectCashRecharge(paramMap);
    }
}
