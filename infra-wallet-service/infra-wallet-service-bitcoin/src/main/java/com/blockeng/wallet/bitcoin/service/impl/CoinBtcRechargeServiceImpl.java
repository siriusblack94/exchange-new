package com.blockeng.wallet.bitcoin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.bitcoin.service.CoinBtcRechargeService;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.*;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.*;
import com.blockeng.wallet.mapper.CoinRechargeMapper;
import com.clg.wallet.bean.TxData;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.wallet.Omni.OmniNewClient;
import com.clg.wallet.wallet.Omni.bean.BtcBlock;
import com.clg.wallet.wallet.Omni.bean.BtcTransaction;
import com.clg.wallet.wallet.Omni.bean.BtcTxOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
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
public class CoinBtcRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinBtcRechargeService {

    private static final Logger LOG = LoggerFactory.getLogger(CoinBtcRechargeServiceImpl.class);

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private UserAddressService userAddressService;

    @Value("${recharge.count}")
    private int maxCount;

    @Autowired
    private CoinRechargeService coinRechargeService;


    @Autowired
    private WalletCollectTaskService walletCollectTaskService;

    @Autowired
    private AdminAddressService adminAddressService;

    /**
     * 遍历是否有充值服务
     */
    public void searchRecharge(CoinConfig coin) {
        LOG.info("查询充值 coinname:" + coin.getName()+"--cointype"+coin.getCoinType()+"--自动充值状态"+coin.getAutoRecharge());
        if(coin.getAutoRecharge()==1){
            OmniNewClient omniNewClient = (OmniNewClient) clientInfo.getClientFromId(coin.getId());
            int latest = omniNewClient.getBlockCount().toInteger() - coin.getMinConfirm();//获取当前区块高度,减去12
            int currentCount = Integer.valueOf(coinConfigService.lastBlock(coin.getId()));
            if (latest > currentCount) {
                int count = latest - currentCount;
                if (count > maxCount) {//每次最多处理100笔
                    latest = currentCount + maxCount;
                }
                LOG.info(coin.getName() + "区块区块开始,start:" + currentCount + ",end=" + latest);
                recharge(coin, omniNewClient, currentCount, latest);
                LOG.info(coin.getName() + "区块区块结束,start:" + currentCount + ",end=" + latest);
            }
        }
    }

    /**
     * 数字货币充值
     *
     * @param coin
     * @param omniNewClient
     * @param currentCount
     * @param latest
     */
    private void recharge(CoinConfig coin, OmniNewClient omniNewClient, int currentCount, int latest) {
        for (int count = currentCount; count <= latest; count++) {
            LOG.info(coin.getName() + "区块高度:" + count);

            BtcBlock btcBlock = omniNewClient.getBlockByNumber("" + count).toObj(BtcBlock.class);
            if (null != btcBlock && !CollectionUtils.isEmpty(btcBlock.getTx()) && btcBlock.getTx().size() > 0) {
               // LOG.info(coin.getName() + "区块:" + btcBlock.getTx());
               // LOG.info(coin.getName() + "区块:" + btcBlock.getTx().size() );
                for (int i = 1; i < btcBlock.getTx().size(); i++) {
                    BtcTransaction btcTransaction = omniNewClient.getTransactionIn(btcBlock.getTx().get(i)).toObj(BtcTransaction.class);
                    if (null != btcTransaction && !CollectionUtils.isEmpty(btcTransaction.getVout())) {
                        List<BtcTxOut> vout = btcTransaction.getVout();
                        for (BtcTxOut item : vout) {
                            List<String> addressesList = item.getScriptPubKey().getAddresses();
                            if (!CollectionUtils.isEmpty(addressesList)) {
                                for(String address:addressesList){
                                    UserAddress account = userAddressService.getByCoinIdAndAddr(address, coin.getId());
                                    if (null != account) {
                                        LOG.error("查询到一条用户数据,txid是:" + btcBlock.getTx().get(i));
                                        boolean isSuccess = coinRechargeService.addRecord(btcTransaction.getTxid(), account.getAddress(), account.getUserId(), item.getValue(), Constant.INSERT_SUCCESS, coin);
                                        if (isSuccess) {
                                            // 发送消息通知交易平台充值成功
                                            LOG.info("[" + coin.getName() + "]receiveConfirmed", "userId, txid, amount", new Object[]{account.getId(), btcTransaction.getTxid(), btcTransaction.getAmount()});
                                            addBackTask(coin, account.getUserId());
                                        } else {
                                            LOG.info("充值失败,txid:" + btcTransaction.getTxid());
                                        }
                                    }
                                }
                            } else {
                            //    LOG.error("输出地址为空,txid:" + btcBlock.getTx().get(i));
                            }
                        }
                    } else {
                      //  LOG.error("txid是:" + btcBlock.getTx().get(i) + "输出为空");

                    }
                }
            } else {
                LOG.error("区块高度:" + count + "查询失败");
            }
        }
        coinConfigService.updateCoinLastblock(latest + "", coin.getId());
    }


    private void addBackTask(CoinConfig config, Long userId) {
        Client client = clientInfo.getClientFromId(config.getId());
        AdminAddress backAdminAddress = adminAddressService.queryAdminAccount(config.getCoinType(), Constant.ADMIN_ADDRESS_TYPE_COLLECT);//归账

        if (null != backAdminAddress && !StringUtils.isEmpty(backAdminAddress.getAddress())) {
            BigDecimal payBalance = BigDecimal.ZERO;
            payBalance = client.getBalance().toBigDecimal();//打款账户余额,一般就是当前钱包余额
            BigDecimal creditLimit = config.getCreditLimit();//交易所钱包留存的款
            BigDecimal creditMaxLimit = config.getCreditMaxLimit();
            if (null != creditMaxLimit && creditMaxLimit.compareTo(BigDecimal.ZERO) > 0 && payBalance.compareTo(creditMaxLimit) > 0) {//如果这样需要更改归账地址
                BigDecimal limit = payBalance;
                if (null != creditLimit) {
                    limit = limit.subtract(creditLimit);
                }
                String txid = client.sendNormalIn(new TxData().
                        setToAddress(backAdminAddress.getAddress()).
                        setPass(config.getWalletPass()).
                        setBalance(limit)).getResult().toString();
                if (!StringUtils.isEmpty(txid))
                    walletCollectTaskService.insert(new WalletCollectTask().setAmount(limit).setUserId(userId).setCoinId(config.getId()).
                            setToAddress(backAdminAddress.getAddress()).
                            setStatus(1).setMark("归集到归账地址成功").setTxid(txid));
            }
        }
    }
}
