package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.utils.GsonUtils;
import com.clg.wallet.bean.ClientBean;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.*;
import com.blockeng.wallet.mapper.CoinRechargeMapper;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.service.CoinRechargeService;
import com.blockeng.wallet.service.WalletCollectTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinRechargeService {


    private static final Logger LOG = LoggerFactory.getLogger(CoinRechargeServiceImpl.class);

    @Autowired
    private AdminAddressService adminAddressService;


    @Autowired
    private WalletCollectTaskService walletCollectTaskService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public boolean addCollectTask(ClientBean clientBean, CoinRecharge coinRecharge, String type) {
        BigDecimal mum = coinRecharge.getAmount();
        if ("eth".equalsIgnoreCase(coinRecharge.getCoinName())) {
            EthNewClient client = (EthNewClient) ClientFactory.getClient(clientBean);
            mum = mum.subtract(client.getEthFee());
            if (BigDecimal.ZERO.compareTo(mum) >= 0) {
                LOG.info("归账金额不足,无需归账,userid:" + coinRecharge.getUserId());
                return true;//无需归集
            }
        }
        WalletCollectTask task = new WalletCollectTask();
        task.setCoinId(coinRecharge.getCoinId())
                .setUserId(coinRecharge.getUserId())
                .setCoinName(coinRecharge.getCoinName())
                .setFromAddress(coinRecharge.getAddress())
                .setCoinType(type);
        AdminAddress payAdminAddress = adminAddressService.queryAdminAccount(clientBean.getCoinType(), Constant.ADMIN_ADDRESS_TYPE_PAY);//获取打款账户信息
        if (!StringUtils.isEmpty(payAdminAddress.getAddress())) {
            task.setToAddress(payAdminAddress.getAddress());
        } else {
            return false;
        }

        task.setAmount(mum);
        return walletCollectTaskService.insert(task);
    }


    public boolean addRecord(String hash, String to, Long userId, BigDecimal money, int confirm, CoinConfig coin) {
        List txList = getByTxid(hash);
        if (null == txList || txList.size() <= 0) {
            LOG.info("查询到一条充值记录,交易id:" + hash + " 币种名称:" + coin.getName());
            CoinRecharge inRecord = new CoinRecharge();
            inRecord.setUserId(userId);
            inRecord.setAddress(to);
            inRecord.setAmount(money);
            inRecord.setTxid(hash);
            inRecord.setCoinType(coin.getCoinType());
            inRecord.setCoinId(coin.getId());
            inRecord.setCoinName(coin.getName());
            inRecord.setConfirm(confirm);
            if (super.insert(inRecord)) {
                rabbitTemplate.convertAndSend(MessageChannel.COIN_RECHARGE_MSG.getName(), GsonUtils.toJson(inRecord));
                return true;
            }
            return false;
        } else {
            LOG.info("不要重复充值,交易id:" + hash);
            return false;
        }
    }


    @Override
    public List getByTxid(String txid) {
        EntityWrapper<CoinRecharge> ew = new EntityWrapper<>();
        ew.eq("txid", txid);
        return super.selectList(ew);
    }

    @Override
    public boolean updateInsertWallet(Long id, int status) {
        return super.updateById(new CoinRecharge().setId(id).setStatus(status));
    }

    @Override
    public boolean updateInsertWalletCommit(Long id, int confirm) {
        return super.updateById(new CoinRecharge().setId(id).setConfirm(confirm));
    }

    @Override
    public List<CoinRecharge> getNotDealInWallet() {
        EntityWrapper<CoinRecharge> ew = new EntityWrapper<>();
        ew.ne("confirm", 1);
        ew.ne("txid", "");
        ew.ne("status", -1);
        return super.selectList(ew);
    }

    @Override
    public List<CoinRecharge> getNotDealInWalletByType(String coinType) {
        EntityWrapper<CoinRecharge> ew = new EntityWrapper<>();
        ew.ne("confirm", 1);
        ew.ne("txid", "");
        ew.ne("status", -1);
        ew.eq("coin_type", coinType);
        return super.selectList(ew);
    }
}
