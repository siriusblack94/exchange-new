package com.blockeng.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.MakeUpRechargeDTO;
import com.blockeng.admin.dto.UserWalletDTO;
import com.blockeng.admin.entity.Account;
import com.blockeng.admin.entity.CoinRecharge;
import com.blockeng.admin.entity.UserWallet;
import com.blockeng.admin.entity.WalletCoinRecharge;
import com.blockeng.admin.mapper.UserWalletMapper;
import com.blockeng.admin.service.CoinRechargeService;
import com.blockeng.admin.service.UserWalletService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.admin.service.WalletCoinRechargeService;
import com.blockeng.framework.utils.GsonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 用户钱包表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Autowired
    private WalletCoinRechargeService walletCoinRechargeService;

    @Autowired
    private CoinRechargeService coinRechargeService;


    @Autowired
    private AccountServiceImpl accountServiceImpl;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Page<UserWalletDTO> selectUserWalletList(Page<UserWalletDTO> page, Long id) {
        return page.setRecords(baseMapper.selectUserWalletList(page, id));
    }

    @Override
    public ResultMap updateRecharge(MakeUpRechargeDTO makeUpRecharge) {
        String address = makeUpRecharge.getAddress();
        String txId = makeUpRecharge.getTxId();

        EntityWrapper<Account> ewAccount = new EntityWrapper<>();
        ewAccount.eq("rec_addr", address);
        Account account = accountServiceImpl.selectOne(ewAccount);
        if (null == account) {
            return ResultMap.getFailureResult("不存在该用户地址");
        }
        EntityWrapper<CoinRecharge> ew = new EntityWrapper<>();
        ew.eq("address", address);
        ew.eq("txid", txId);
        CoinRecharge coinRecharge = coinRechargeService.selectOne(ew);

        if (null == coinRecharge) {
            EntityWrapper<WalletCoinRecharge> walletEw = new EntityWrapper<>();
            walletEw.eq("txid", txId);
            walletEw.eq("address", address);
            WalletCoinRecharge walletCoinRecharge = walletCoinRechargeService.selectOne(walletEw);
            if (walletCoinRecharge == null) {
                return ResultMap.getFailureResult("钱包库中不存在当前订单,请检查txId,请联系开发,检查钱包服务器状态");
            }
            rabbitTemplate.convertAndSend("finance.recharge.success", GsonUtil.toJson(walletCoinRecharge));
            return ResultMap.getSuccessfulResult();
        } else {
            return ResultMap.getFailureResult("订单已经存在,补单失败");
        }
    }
}
