package com.blockeng.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.entity.Account;
import com.blockeng.entity.CashWithdrawals;
import com.blockeng.entity.Config;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CashWithdrawStatus;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.mapper.CashWithdrawalsMapper;
import com.blockeng.service.AccountService;
import com.blockeng.service.CashWithdrawalsService;
import com.blockeng.service.ConfigService;
import com.blockeng.service.RandCodeService;
import com.blockeng.user.dto.UserBankDTO;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserBankServiceClient;
import com.blockeng.user.feign.UserServiceClient;
import com.blockeng.vo.CashWithDrawalsForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * <p>
 * 提现表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class CashWithdrawalsServiceImpl extends ServiceImpl<CashWithdrawalsMapper, CashWithdrawals> implements CashWithdrawalsService, Constant {

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private UserBankServiceClient userBankServiceClient;

    @Autowired
    private AccountService accountService;

    @Autowired
    private RandCodeService randCodeService;

    /**
     * 法币提现
     *
     * @param cashWithdrawals 法币提现请求参数
     * @param
     * @return
     */
    @Override
    @Transactional
    public Response c2cSell(CashWithDrawalsForm cashWithdrawals, UserDetails userDetails) {
        long userId = userDetails.getId();
        // 系统参数提现开关
        Config config = configService.queryByTypeAndCode(Constant.CONFIG_TYPE_CNY, CONFIG_WITHDRAW_STATUS);
        int status = Integer.valueOf(config.getValue());
        if (status != 1) {
            log.error("提现开关已关闭");
            throw new GlobalDefaultException(2025);
        }
        // 获取当前登录缓存用户的信息  短信验证码
        UserDTO user = userServiceClient.selectById(userId);
        if (user.getAuthStatus() == null || user.getAuthStatus() != 2) {
            log.error("用户尚未完成高级实名认证");
            throw new GlobalDefaultException(50029);
        }
        // 用户提现银行卡
        UserBankDTO bank = userBankServiceClient.selectByUserId(userId);
        if (!Optional.ofNullable(bank).isPresent()) {
            log.error("未添加银行卡");
            throw new GlobalDefaultException(2026);
        }
        if (cashWithdrawals.getNum().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("提现金额必须大于0");
            //throw new GlobalDefaultException(2014);
            return Response.err(2014, "提现金额必须大于0");
        }
        config = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_USDT2CNY);
        // 获取换算比率
        BigDecimal usdt2cnyRate = new BigDecimal(config.getValue());
        // 计算提现金额
        BigDecimal withdrawAmount = usdt2cnyRate.multiply(cashWithdrawals.getNum()).setScale(2, BigDecimal.ROUND_DOWN);
        // 最大提现金额（USDT）
        config = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_WITHDRAW_MAX_AMOUNT);
        BigDecimal maxAmount = new BigDecimal(config.getValue());
        if (cashWithdrawals.getNum().compareTo(maxAmount) > 0) {
            log.error("提现金额超出系统最大限额");
            //throw new GlobalDefaultException(50050);
            return Response.err(50050, "提现金额超出系统最大限额" + maxAmount);
        }
        // 最小提现金额（USDT）
        config = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_WITHDRAW_MIN_AMOUNT);
        BigDecimal minAmount = StringUtils.isEmpty(config.getValue()) ? BigDecimal.ZERO : new BigDecimal(config.getValue());
        if (cashWithdrawals.getNum().compareTo(minAmount) < 0) {
            log.error("提现金额小于系统最小限额");
            //throw new GlobalDefaultException(50051);
            return Response.err(50051, "提现金额小于系统最小限额" + minAmount);
        }
        // 当日提现金额（USDT）
        config = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_WITHDRAW_DAY_MAX_AMOUNT);
        BigDecimal maxAmountPerDay = StringUtils.isEmpty(config.getValue()) ? BigDecimal.ZERO : new BigDecimal(config.getValue());
        BigDecimal poundage;
        if (maxAmountPerDay.doubleValue() > 0.0D) {
            BigDecimal total = baseMapper.getTotalByUserId(user.getId(), DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
            poundage = maxAmountPerDay.subtract(total);
            poundage = poundage.doubleValue() < 0.0D ? BigDecimal.ZERO : poundage;
            if (cashWithdrawals.getNum().min(poundage) != cashWithdrawals.getNum()) {
                log.error("当日提现金额超出系统最大限额");
                //throw new GlobalDefaultException(50052);
                return Response.err(50052, "当日提现金额超出系统最大限额" + maxAmountPerDay);
            }
        }
        // 手续费
        poundage = getWithdrawalsPoundage(withdrawAmount);
        // 资金账户
        Account account = accountService.queryByUserIdAndCoinId(user.getId(), cashWithdrawals.getCoinId());
        if (account.getStatus() == 2) {
            log.error("用户已冻结");
            throw new GlobalDefaultException(2024);
        }
        // 校验交易密码
        String payPassword = cashWithdrawals.getPayPassword();
        if (!new BCryptPasswordEncoder().matches(payPassword, user.getPaypassword())) {
            log.error("交易密码错误");
            throw new GlobalDefaultException(2012);
        }

        // 校验手机验证码
        RandCodeVerifyDTO randCodeVerifyDTO = new RandCodeVerifyDTO()
                .setCountryCode(user.getCountryCode())
                .setTemplateCode(SmsTemplate.CASH_WITHDRAWS.getCode())
                .setPhone(user.getMobile())
                .setEmail(user.getEmail())
                .setCode(cashWithdrawals.getValidateCode());
        boolean flag = randCodeService.verify(randCodeVerifyDTO);
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        }
        CashWithdrawals withdrawals = new CashWithdrawals();
        withdrawals.setAccountId(account.getId())                   // 资金账户ID
                .setUserId(user.getId())                            // 用户ID
                .setCoinId(cashWithdrawals.getCoinId())             // 币种ID
                .setBank(bank.getBank())                            // 银行卡ID
                .setBankAddr(bank.getBankAddr())                    // 开户行地址
                .setBankCard(bank.getBankCard())                    // 卡号
                .setTruename(bank.getRealName())                    // 开户人姓名
                .setFee(poundage)                                   // 提现手续费
                .setNum(cashWithdrawals.getNum())                   // 提现数量USDT
                .setMum(withdrawAmount.subtract(poundage))          // 提现金额
                .setStep(1)                                         // 当前审核级数
                .setStatus(CashWithdrawStatus.PENDING.getCode());   // 状态
        baseMapper.insert(withdrawals);
        // 冻结资金
        accountService.lockAmount(userId, cashWithdrawals.getCoinId(), cashWithdrawals.getNum(), BusinessType.WITHDRAW, withdrawals.getId());
        return Response.ok();
    }

    /**
     * 获取提现手续费
     *
     * @param amount 提现金额
     * @return
     */
    BigDecimal getWithdrawalsPoundage(BigDecimal amount) {
        if (amount == null) {
            return BigDecimal.ZERO;
        } else {
            String minPoundageConfig = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_WITHDRAW_MIN_POUNDAGE).getValue();
            BigDecimal minPoundage = StringUtils.isEmpty(minPoundageConfig) ? new BigDecimal(0) : new BigDecimal(minPoundageConfig);
            String poundageRateConfig = configService.queryByTypeAndCode(CONFIG_TYPE_CNY, CONFIG_WITHDRAW_POUNDAGE_RATE).getValue();
            BigDecimal poundageRate = StringUtils.isEmpty(poundageRateConfig) ? new BigDecimal(0) : new BigDecimal(poundageRateConfig);
            BigDecimal value = amount.multiply(poundageRate);
            return value.min(minPoundage).equals(value) ? minPoundage : value;
        }
    }
}
