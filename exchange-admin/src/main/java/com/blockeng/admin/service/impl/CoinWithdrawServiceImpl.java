package com.blockeng.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.bean.CoinWithdrawBean;
import com.blockeng.admin.dto.*;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.enums.MessageChannel;
import com.blockeng.admin.mapper.CoinWithdrawMapper;
import com.blockeng.admin.service.*;
import com.blockeng.admin.web.vo.mappers.CoinWithdrawRetryRecordMapper;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CoinWithdrawStatus;
import com.blockeng.framework.exception.AccountException;
import com.blockeng.framework.exception.ExchangeException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.GsonUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
 * 虚拟币提现 服务实现类
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-17
 */
@Service
@Slf4j
public class CoinWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinWithdrawService, Constant {

    @Autowired
    private CoinWithdrawAuditRecordService coinWithdrawAuditRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private MultiLevelAuditService multiLevelAuditService;

    @Autowired
    private LockService lockService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinWithdrawRetryRecordService coinWithdrawRetryRecordService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public Page<CoinWithdraw> selectListPage(Page<CoinWithdraw> page, Wrapper<CoinWithdraw> wrapper) {
        //wrapper = (Wrapper<CoinWithdraw>) SqlHelper.fillWrapper(page, wrapper);
        page.setRecords(baseMapper.selectListPage(page, wrapper));
        return page;
    }


    /**
     * 提币审核
     *
     * @param auditDTO 提币审核请求参数
     * @param sysUser  当前登录用户
     * @throws ExchangeException
     */
    @Override
    @Transactional
    public void coinWithdrawAudit(AuditDTO auditDTO, SysUser sysUser) throws ExchangeException {
        log.info("coinWithdrawAudit auditDTO:" + auditDTO);
        // 校验权限
        if (!multiLevelAuditService.coinWithdrawPermissionCheck(auditDTO.getId(), sysUser)) {
            throw new ExchangeException("审核权限不足");
        }
        boolean isLocked = false;
        try {
            // 通过内存锁防止重复提交审核，导致资金异常
            isLocked = lockService.getLock(REDIS_KEY_COIN_WITHDRAW_AUDIT_LOCK, String.valueOf(auditDTO.getId()), false);
            if (!isLocked) {
                throw new ExchangeException("已经提交审核，请勿重复操作");
            }
            CoinWithdraw coinWithdraw = baseMapper.selectById(auditDTO.getId());
            if (coinWithdraw == null) {
                throw new ExchangeException("提现订单不存在");
            }
            if (coinWithdraw.getStatus() != CoinWithdrawStatus.PENDING.getCode()) {
                throw new ExchangeException("此记录已审核");
            }
            // 审核轨迹
            CoinWithdrawAuditRecord coinRechargeAuditRecord = new CoinWithdrawAuditRecord();
            coinRechargeAuditRecord.setOrderId(auditDTO.getId())
                    .setStatus(auditDTO.getStatus())
                    .setRemark(auditDTO.getRemark())
                    .setStep(coinWithdraw.getStep())
                    .setAuditUserId(sysUser.getId())
                    .setAuditUserName(sysUser.getFullname());
            coinWithdrawAuditRecordService.insert(coinRechargeAuditRecord);
            User user = userService.selectById(coinWithdraw.getUserId());
            // 更新提现申请单状态
            coinWithdraw.setRemark(auditDTO.getRemark());
            if (auditDTO.getStatus().intValue() == CoinWithdrawStatus.REFUSE.getCode()) {
                // 审核拒绝
                coinWithdraw.setStatus(CoinWithdrawStatus.REFUSE.getCode())
                        .setMum(BigDecimal.ZERO)
                        .setAuditTime(new Date());
                baseMapper.updateById(coinWithdraw);
                // 解冻资金账户
                accountService.unlockAmount(coinWithdraw.getUserId(),
                        coinWithdraw.getCoinId(),
                        coinWithdraw.getNum(),
                        BusinessType.WITHDRAW,
                        coinWithdraw.getId());
                return;
            }
            // 审核通过
            Config config = configService.queryBuyCodeAndType(CONFIG_TYPE_SYSTEM, Constant.CONFIG_COIN_WITHDRAW_AUDIT_STEPS);
            if (config == null || Strings.isNullOrEmpty(config.getValue())) {
                throw new ExchangeException("没有配置审核级数");
            }
            int step = coinWithdraw.getStep();
            if (step == Integer.valueOf(config.getValue()).intValue()) {
                // 最终审核通过
                CoinConfig coinConfig = coinConfigService.selectById(coinWithdraw.getCoinId());
                if (null == coinConfig) {
                    throw new ExchangeException("没有查询到当前币种");
                }
                coinWithdraw.setStatus(CoinWithdrawStatus.PASSED.getCode());
                if (1 == coinConfig.getAutoDraw().intValue() &&
                        (null == coinConfig.getAutoDrawLimit()
                                || coinConfig.getAutoDrawLimit().compareTo(BigDecimal.ZERO) <= 0
                                || coinConfig.getAutoDrawLimit().compareTo(coinWithdraw.getNum()) >= 0)) { //自动提款
                    coinWithdraw.setType(1);
                    notifyAuditResult(coinWithdraw);//自动打币
                } else { //手工提币
                    coinWithdraw.setType(2); //2手工打币
                }
                coinWithdraw.setAuditTime(new Date());
                baseMapper.updateById(coinWithdraw);
                return;
            }
            // 不是最终审核，审计级别加一级，状态为待审核
            coinWithdraw.setStatus(CoinWithdrawStatus.PENDING.getCode()).setStep(step + 1);
            baseMapper.updateById(coinWithdraw);
            return;
        } catch (AccountException e) {
            throw new ExchangeException(e.getMessage());
        } finally {
            // 释放锁
            if (isLocked) {
                lockService.unlock(REDIS_KEY_COIN_WITHDRAW_AUDIT_LOCK, String.valueOf(auditDTO.getId()));
            }
        }
    }

    /**
     * 数字货币提现审核结果通知
     *
     * @param coinWithdraw 提现申请单
     * @return
     */
    public void notifyAuditResult(CoinWithdraw coinWithdraw) {
        if (coinWithdraw.getStatus() == CoinWithdrawStatus.PASSED.getCode()) {//通知钱包服务器打币
            Coin coin = coinService.selectById(coinWithdraw.getCoinId());
            String type = coin.getType();
            type = type.replace("Token", "");
            log.error("自动提币发消息-----:" + JSON.toJSON(coinWithdraw));
            rabbitTemplate.convertAndSend("finance.withdraw.send." + type, JSON.toJSON(coinWithdraw));
        }
    }

    @Override
    public List<Map<String, Object>> selectCoinWithdrawGroupCoin(String userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", 1);
        return baseMapper.selectCoinWithdrawGroupCoin(paramMap);
    }

    public Page<CoinWithdrawalsCountDTO> selectCountMain(Page<CoinWithdrawalsCountDTO> page, Map<String, Object> paramMap) {
        return page.setRecords(baseMapper.selectCountMain(page, paramMap));
    }

    @Override
    public List<CoinWithdrawalsCountDTO> selectValidCounts(Map<String, Object> paramMap) {
        return baseMapper.selectValidCounts(paramMap);
    }

    @Override
    public List<CoinWithdrawalsCountDTO> selectUserCt(Map<String, Object> paramMap) {
        return baseMapper.selectUserCt(paramMap);
    }

    @Override
    public Object withDrawSuccess(CoinWithDrawDTO coinWithDrawDTO) {
        CoinWithdraw coinWithdraw = selectById(coinWithDrawDTO.getId());
        if (null == coinWithdraw) {
            return Response.err(50034, "未找到该笔体现订单");
        }
        coinWithdraw.setStatus(5).setTxid(coinWithDrawDTO.getTxid()).setChainFee(coinWithDrawDTO.getChainFee());
        CoinWithdrawBean coinWithdrawBean = new CoinWithdrawBean();
        coinWithdrawBean.setResult(coinWithdraw);
        // rabbitTemplate.convertSendAndReceive(MessageChannel.FINANCE_WITHDRAW_RESULT.getChannel(),GsonUtil.toJson(coinWithdraw));
        rabbitTemplate.convertAndSend(MessageChannel.FINANCE_WITHDRAW_RESULT.getChannel(), GsonUtil.toJson(coinWithdrawBean));
        return Response.ok();
    }

    /**
     * 重新打币,插入记录
     *
     * @param
     */
    @Override
    @Transactional
    public Integer reTryWithdraw(AuditDTO auditDTO) {
        if (auditDTO.getStatus() == 6) {
            CoinWithdraw coinWithdraw = baseMapper.selectById(auditDTO.getId());
            if (coinWithdraw != null) {
                if (coinWithdraw.getStatus() == 6) {
                    try {
                        CoinWithdrawRetryRecord record = CoinWithdrawRetryRecordMapper.INSTANCE.from(coinWithdraw);
                        record.setCreated(new Date()).setId(null).setOrderId(coinWithdraw.getId());
//                      //查询一级审核人员和二级审核人员
//                      CoinWithdrawAuditRecord coinWithdrawAuditRecord = coinWithdrawAuditRecordService.selectById(record.getId());
                        coinWithdrawRetryRecordService.insert(record);
                        coinWithdraw.setStatus(4).setTxid(StringUtils.EMPTY).setWalletMark(StringUtils.EMPTY);
                        return baseMapper.updateById(coinWithdraw);
                    } catch (Exception e) {
                        log.info("***exception:" + e);
                    }
                }
            } else {
                throw new ExchangeException("订单不存在");
            }
        }
        return 0;
    }


    @Override
    public DigitalCoinWithdrawStatisticsDTO selectDigitalCoinWithdrawStatistics(int current, int size, String startTime, String endTime, String coinId, String userId) {

        BigDecimal withdrawCounts = new BigDecimal(0);
        BigDecimal fee = new BigDecimal(0);
        BigDecimal realWithdrawCounts = new BigDecimal(0);
        Long withdrawTimes = Long.valueOf(0);
        Long total = Long.valueOf(0);

        Coin coin = coinService.selectById(coinId);
        DigitalCoinWithdrawStatisticsDTO digitalCoinWithdrawStatisticsDTO = new DigitalCoinWithdrawStatisticsDTO();
        Page<DigitalCoinWithdrawStatistics> page = new Page<>(current, size);

        Map<String, Object> paramMap = new HashMap<>();
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(endTime)) {
//            endTime = endTime + " 23:59:59";
//        }
//        if (org.apache.commons.lang3.StringUtils.isNotBlank(endTime)) {
////            endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
////        }
        if (!Strings.isNullOrEmpty(endTime)) {
            endTime = endTime + " 23:59:59";
        }

        paramMap.put("startTime", startTime);
        paramMap.put("endTime", endTime);
        paramMap.put("coinId", coinId);
        paramMap.put("userId", userId);

        //用户明细
        List<DigitalCoinWithdrawStatistics> digitalCoinWithdrawStatisticsList = baseMapper.selectDigitalCoinWithdrawStatistics(page, paramMap);
        //总体统计
        Map<String, Object> resultMap = baseMapper.countDigitalCoinWithdrawCountsAndTimes(paramMap);

        if (digitalCoinWithdrawStatisticsList != null && digitalCoinWithdrawStatisticsList.size() > 0) {

            digitalCoinWithdrawStatisticsList.forEach(item -> {
                item.setCoinName(coin.getName());
            });

            withdrawCounts = (BigDecimal) resultMap.get("withdrawCount");
            fee = (BigDecimal) resultMap.get("fee");
            realWithdrawCounts = (BigDecimal) resultMap.get("realWithdrawCount");
            withdrawTimes = (Long) resultMap.get("withdrawTimes");
            total = (Long) resultMap.get("total");
            digitalCoinWithdrawStatisticsDTO.setRecords(digitalCoinWithdrawStatisticsList);
        }

        digitalCoinWithdrawStatisticsDTO.setCoinName(coin.getName());
        digitalCoinWithdrawStatisticsDTO.setFee(fee);
        digitalCoinWithdrawStatisticsDTO.setWithdrawCount(withdrawCounts);
        digitalCoinWithdrawStatisticsDTO.setRealWithdrawCount(realWithdrawCounts);
        digitalCoinWithdrawStatisticsDTO.setWithdrawTimes(withdrawTimes.intValue());
        digitalCoinWithdrawStatisticsDTO.setCurrent(current).setSize(size).setTotal(total.intValue());

        return digitalCoinWithdrawStatisticsDTO;
    }

    @Override
    public List<Map<String, Object>> selectCoinWithdrawFreezeGroupCoin(String userId) {
        //冻结状态位 分别对应 [0.审核中|4.审核通过|5.打币中] 先这么留着 看是不是写到表里 写死还是有点尴尬
        String freezeStatus = "0,4,5";
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("status", freezeStatus);
        return baseMapper.selectCoinWithdrawFreezeGroupCoin(paramMap);
    }

    @Override
    public BigDecimal selectAmountByCoinId(String id) {
        return baseMapper.selectAmountByCoinId(id);
    }
}
