package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.config.Constant;
import com.clg.wallet.bean.ClientBean;
import com.blockeng.wallet.entity.AdminAddress;
import com.blockeng.wallet.entity.ClientBeanMapper;
import com.blockeng.wallet.entity.CoinBalance;
import com.blockeng.wallet.entity.CoinConfig;
import com.clg.wallet.enums.CoinType;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.CoinBalanceMapper;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.blockeng.wallet.service.AdminAddressService;
import com.blockeng.wallet.service.CoinBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 币种余额 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
public class CoinBalanceServiceImpl extends ServiceImpl<CoinBalanceMapper, CoinBalance> implements CoinBalanceService {


    private static final Logger LOG = LoggerFactory.getLogger(CoinBalanceServiceImpl.class);

    @Autowired
    private ClientInfo clientInfo;

    private boolean sendFeeMsg = false;//为了保证短信仅仅发送一次

    @Autowired
    private AdminAddressService adminAddressService;


    public void checkBalance(String type) {

        ClientBean clientBean = clientInfo.getClientInfoFromType(type);
        if (null != clientBean) {
            Client client = ClientFactory.getClient(clientBean);
            saveBalance(client, clientBean);
            String typeToken = type + "Token";
            HashMap<String, List<CoinConfig>> typeCoinMap = clientInfo.getTypeCoinMap();
            List<CoinConfig> coinConfigList = typeCoinMap.get(typeToken);
            if (!CollectionUtils.isEmpty(coinConfigList)) {//说明有Token币种
                ClientBean mainClientBean = clientInfo.getClientInfoFromType(type);
                for (CoinConfig coin : coinConfigList) {
                    saveTokenBalance(client, mainClientBean, coin);
                }
            }
        } else {
            LOG.error("未找到当前币种");
        }
    }

    private void saveBalance(Client client, ClientBean clientBean) {
        BigDecimal collectBalance = BigDecimal.ZERO;
        BigDecimal loanBalance = BigDecimal.ZERO;
        BigDecimal feeBalance = BigDecimal.ZERO;
        BigDecimal rechargeBalance = BigDecimal.ZERO;

        if (clientBean.getCoinType().equalsIgnoreCase(CoinType.BTC)) {//当前打币余额
            loanBalance = client.getBalance().toBigDecimal();
        } else {
            AdminAddress loadAddress = adminAddressService.queryAdminAccount(clientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_PAY);
            if (null != loadAddress && !StringUtils.isEmpty(loadAddress.getAddress())) {
                loanBalance = client.getBalance(loadAddress.getAddress()).toBigDecimal();
            }
        }

        AdminAddress collectAddress = adminAddressService.queryAdminAccount(clientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_COLLECT);
        if (null != collectAddress && !StringUtils.isEmpty(collectAddress.getAddress())) {
            collectBalance = client.getBalance(collectAddress.getAddress()).toBigDecimal();
        }

        AdminAddress feeAddress = adminAddressService.queryAdminAccount(clientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_FEE);
        if (clientBean.getCoinType().equalsIgnoreCase(CoinType.ETH) && null != feeAddress && !StringUtils.isEmpty(feeAddress.getAddress())) {
            feeBalance = client.getBalance(feeAddress.getAddress()).toBigDecimal();

            if (feeBalance.compareTo(BigDecimal.valueOf(0.5)) < 0 && !sendFeeMsg) {//当手续费小于0.5的时候,发送短信预警
                //TODO  加入短信功能,手续费余额不足啦
                sendFeeMsg = true;
            } else if (feeBalance.compareTo(BigDecimal.valueOf(0.5)) >= 0) {
                sendFeeMsg = false;
            }
        }

        AdminAddress rechargeAddress = adminAddressService.queryAdminAccount(clientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_RECHARGE);
        if (null != rechargeAddress && !StringUtils.isEmpty(rechargeAddress.getAddress())) {//有充值地址
            rechargeBalance = client.getBalance(rechargeAddress.getAddress()).toBigDecimal();

        }

        insertBalance(clientBean, feeBalance, collectBalance, loanBalance, rechargeBalance);
    }

    /**
     * @param client         主钱包客户端
     * @param mainClientBean 主钱包客户端信息
     * @param coin           代币的信息
     */
    private void saveTokenBalance(Client client, ClientBean mainClientBean, CoinConfig coin) {

        BigDecimal collectBalance = BigDecimal.ZERO;
        BigDecimal loanBalance = BigDecimal.ZERO;

        AdminAddress homeAddress = adminAddressService.queryAdminAccount(mainClientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_COLLECT);

        if (null != homeAddress && !StringUtils.isEmpty(homeAddress.getAddress())) {
            collectBalance = client.getTokenBalance(coin.getContractAddress(), homeAddress.getAddress()).toBigDecimal();
        }

        AdminAddress loadAddress = adminAddressService.queryAdminAccount(mainClientBean.getId(), Constant.ADMIN_ADDRESS_TYPE_PAY);
        if (null != loadAddress && !StringUtils.isEmpty(loadAddress.getAddress())) {
            collectBalance = client.getTokenBalance(coin.getContractAddress(), loadAddress.getAddress()).toBigDecimal();
        }
        insertBalance(ClientBeanMapper.INSTANCE.form(coin), null, BigDecimal.ZERO, collectBalance, loanBalance);
    }

    private void insertBalance(ClientBean clientBean, BigDecimal rechargeBalance, BigDecimal feeBalance, BigDecimal collectBalance, BigDecimal loadBalance) {
        CoinBalance coinBalance = new CoinBalance();
        coinBalance.setCoinId(clientBean.getId())
                .setCoinName(clientBean.getName())
                .setRechargeAccountBalance(rechargeBalance)
                .setFeeAccountBalance(feeBalance)
                .setCollectAccountBalance(collectBalance)
                .setLoanAccountBalance(loadBalance)
                .setCoinType(clientBean.getCoinType());
        Long id = selectByCoinId(clientBean.getId());
        if (id > 0) {
            coinBalance.setId(id);
        }
        if (super.insertOrUpdate(coinBalance)) {
            LOG.info("更新实时账户余额成功");
        } else {
            LOG.info("更新实时账户余额失败,币种名称:" + clientBean.getName());
        }
    }

    public Long selectByCoinId(Long coinId) {
        EntityWrapper<CoinBalance> ew = new EntityWrapper<>();
        ew.eq("coin_id", coinId);
        CoinBalance coinBalance = super.selectOne(ew);
        if (null != coinBalance) {
            return coinBalance.getId();
        }
        return 0L;
    }

}
