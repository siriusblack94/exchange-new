package com.blockeng.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.clg.wallet.bean.ClientBean;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinRecharge;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface CoinRechargeService extends IService<CoinRecharge> {

    List getByTxid(String hash);


    boolean addRecord(String hash, String to, Long userId, BigDecimal money, int confirm, CoinConfig coin);

    boolean addCollectTask(ClientBean clientBean, CoinRecharge userInWallet, String type);

    boolean updateInsertWallet(Long id, int type);

    boolean updateInsertWalletCommit(Long id, int status);


    List<CoinRecharge> getNotDealInWallet();

    List<CoinRecharge> getNotDealInWalletByType(String coinType);

}
