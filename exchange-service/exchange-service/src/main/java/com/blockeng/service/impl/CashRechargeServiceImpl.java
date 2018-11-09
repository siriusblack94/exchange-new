package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.entity.*;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.enums.AdminBankStatus;
import com.blockeng.framework.enums.CashRechargeStatus;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.mapper.CashRechargeMapper;
import com.blockeng.service.*;
import com.blockeng.vo.CashRechargeForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 法币充值表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class CashRechargeServiceImpl extends ServiceImpl<CashRechargeMapper, CashRecharge> implements CashRechargeService, Constant {

    @Autowired
    private AdminBankService adminBankService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CoinService coinService;

    @Override
    @Transactional
    public Response c2cBuy(CashRechargeForm cashRechargeForm, long userId) {
        QueryWrapper<AdminBank> bankWrapper = new QueryWrapper<>();
        bankWrapper.eq("status", AdminBankStatus.ONE.getCode());
        List<AdminBank> adminBankList = adminBankService.selectList(bankWrapper);
        AdminBank adminBank;
        //随机一张银行卡
        if (adminBankList.isEmpty()) {
            throw new GlobalDefaultException(50056);
        } else if (adminBankList.size() == 1) {
            adminBank = adminBankList.get(0);
        } else {
            Random ra = new Random();
            int index = ra.nextInt(adminBankList.size() - 1);
            adminBank = adminBankList.get(index);
        }

        // 资金账户
        Account account = accountService.queryByUserIdAndCoinId(userId, cashRechargeForm.getCoinId());
        if (!Optional.ofNullable(account).isPresent()) {
            throw new GlobalDefaultException(2023);
        }
        if (account.getStatus() == 2) {
            throw new GlobalDefaultException(2024);
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
        String orderDatetime = df.format(new Date());
        String tradeNo = orderDatetime + ((new Random()).nextInt(90000) + 10000);
        // 获取换算比率
        QueryWrapper<Config> e = new QueryWrapper<>();
        e.eq("code", CONFIG_CNY2USDT);
        Config cny2usdtConfig = configService.selectOne(e);
        String configValue = cny2usdtConfig.getValue();
        BigDecimal cny2usdtRate = new BigDecimal(configValue);
        //查询充值币种
        Coin coin = coinService.selectById(cashRechargeForm.getCoinId());
        // 充值人民币金额
        BigDecimal mum = cny2usdtRate.multiply(cashRechargeForm.getNum()).setScale(2, BigDecimal.ROUND_UP);
        String remark = getRandomCodeString(6);
        CashRecharge cashRecharge = new CashRecharge();
        cashRecharge.setUserId(userId)                              // 用户ID
                .setNum(cashRechargeForm.getNum())                  // 充值购买USDT数量
                .setFee(BigDecimal.ZERO)                            // 手续费
                .setMum(mum)                                        // 充值人民币金额
                .setType("linepay")                                 // 充值方式
                .setTradeno(tradeNo)                                // 充值流水
                .setCoinId(cashRechargeForm.getCoinId())            // 充值购买币种ID
                .setCoinName(coin.getName())               // 充值购买币种名称
                .setStep(1)                                         // 当前审核级数
                .setStatus(CashRechargeStatus.PENDING.getCode())    // 状态,待审核
                .setRemark(remark)                                  // 备注
                .setName(adminBank.getName()).setBankName(adminBank.getBankName()).setBankCard(adminBank.getBankCard());    //银行卡信息
        long id = baseMapper.insert(cashRecharge);
        Map<String, Object> result = new HashMap<>();
        result.put("name", adminBank.getName());
        result.put("bankName", adminBank.getBankName());
        result.put("bankCard", adminBank.getBankCard());
        result.put("amount", mum);
        result.put("status", CashRechargeStatus.PENDING.getCode());
        result.put("remark", remark);
        if (id > 0L) {
            return Response.ok(result);
        }
        throw new GlobalDefaultException(20008);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public String getRandomCodeString(int length) {
        StringBuffer code = new StringBuffer();
        if (length == 0) {
            return code.toString();
        }
        String sources = "0123456789";
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            code.append(sources.charAt(rand.nextInt(sources.length() - 1)));
        }
        return code.toString();
    }
}
