package com.blockeng.wallet.help;


import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.UserAddress;
import com.blockeng.wallet.entity.WalletCollectTask;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.utils.DESUtil;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.EThTransactionBean;
import com.clg.wallet.bean.EthResult;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.help.WalletConstant;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

/**
 * 以太坊客户端
 * Created by Administrator on 2018/1/18.
 */
@Component
public class ETHClient {

    private static final Logger LOG = LoggerFactory.getLogger(ETHClient.class);


    @Autowired
    private ClientInfo actClientInfo;

    @Autowired
    private AdminAddressService adminAddressService;


    @Autowired
    private DESUtil desUtil;

    public EthResult sendEthTokenToCenter(UserAddress userEth, String addr, CoinConfig coin) throws Exception {
        EthNewClient client = (EthNewClient) ClientFactory.getClient(actClientInfo.getClientInfoFromType(CoinType.ETH));
        BigDecimal tokenBalance = client.getTokenBalance(coin.getContractAddress(), userEth.getAddress()).toBigDecimal();

        if (tokenBalance.compareTo(BigDecimal.valueOf(0)) > 0) {
            EThTransactionBean eThTransactionBean = new EThTransactionBean();
            eThTransactionBean
                    .setTokenBalance(tokenBalance)
                    .setFromAddress(userEth.getAddress())
                    .setToAddress(addr)
                    .setAllTransaction(true)
                    .setFromUserKeystore(desUtil.decrypt(userEth.getKeystore()))
                    .setContractAddress(coin.getContractAddress())
                    .setFromUserPass(desUtil.decrypt(userEth.getPwd()));
            EthResult ethResult = client.sentEthToken(eThTransactionBean);
            if (ethResult.isSuccess()) {
                return ethResult;
            } else {
                if (ethResult.getCode() == WalletConstant.NOT_ENOUGH_FEE) {//如果等于-2,代表手续费不够,需要支付手续费
                    AdminAddress feeAdminAddress = adminAddressService.queryAdminAccount(CoinType.ETH, Constant.ADMIN_ADDRESS_TYPE_FEE);//获取手续费账户信息
                    if (null != feeAdminAddress && !StringUtils.isEmpty(feeAdminAddress.getPwd()) && !StringUtils.isEmpty(feeAdminAddress.getKeystore())) {
                        eThTransactionBean.setFromAddress(feeAdminAddress.getAddress())
                                .setFromUserPass(desUtil.decrypt(feeAdminAddress.getPwd()))
                                .setFromUserKeystore(desUtil.decrypt(feeAdminAddress.getKeystore()))
                                .setAllTransaction(false)
                                .setBalance(client.getEthTokenFee())
                                .setToAddress(userEth.getAddress());
                        ethResult = client.sentEth(eThTransactionBean);
                        return ethResult.setCode(WalletConstant.NOT_ENOUGH_FEE).setSuccess(false);
                    } else {
                        throw new RuntimeException("手续费账户为空,或者没有keystore或者password");
                    }

                }
                return ethResult;
            }
        } else {
            LOG.info("数量为0无需归集" + userEth.getAddress());
            return new EthResult().setCode(WalletConstant.NOT_ENOUGH_MONEY).setSuccess(false).setInfo("数量为0无需归集" + userEth.getAddress());
        }
    }


    public EthResult sendEthToCenter(ClientBean clientBean, UserAddress userEth, WalletCollectTask task) throws Exception {
        EthNewClient client = (EthNewClient) ClientFactory.getClient(clientBean);
        if (StringUtils.isEmpty(task.getToAddress())) {
            throw new RuntimeException("归账地址不能为空");
        }
        EThTransactionBean eThTransactionBean = new EThTransactionBean();
        eThTransactionBean.setFromAddress(userEth.getAddress())
                .setFromUserPass(desUtil.decrypt(userEth.getPwd()))
                .setFromUserKeystore(desUtil.decrypt(userEth.getKeystore()))
                .setAllTransaction(true)
                .setBalance(task.getAmount())
                .setToAddress(task.getToAddress());
        return client.sentEth(eThTransactionBean);
    }


}


