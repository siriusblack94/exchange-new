package com.blockeng.wallet.service.impl;

import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.*;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.*;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
//import com.clg.wallet.wallet.act.AchainCore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class FeeServiceImpl implements FeeService {

    private static final Logger LOG = LoggerFactory.getLogger(FeeServiceImpl.class);

    @Autowired
    private CoinWithdrawService coinWithdrawService;


    @Autowired
    private WalletCollectTaskService walletCollectTaskService;

    @Autowired
    private ClientInfo clientInfo;


    public void queryChainFee(Long id, String type) {
        List<CoinWithdraw> withdrawList = coinWithdrawService.queryNoFeeList(id, type);
        List<WalletCollectTask> walletCollectTaskList = walletCollectTaskService.queryNoFeeList(id, type);
        saveWithDrawFee(withdrawList, walletCollectTaskList);
    }

    /**
     * @param withdraws
     */
    private void saveWithDrawFee(List<CoinWithdraw> withdraws, List<WalletCollectTask> collectTaskList) {
        Client client = null;
        if (!CollectionUtils.isEmpty(withdraws)) {
            for (CoinWithdraw item : withdraws) {//循环获取手续费
                if (null == client) {
                    client = clientInfo.getClientFromId(item.getCoinId());
                }
                if (null != client) {
                    String txid = item.getTxid();
                    if (!StringUtils.isEmpty(txid)) {
                        BigDecimal feeBalance = client.getTransactionFee(txid).toBigDecimal();
                        coinWithdrawService.updateById(item.setChainFee(feeBalance));
                    }
                }
            }
        } else if (!CollectionUtils.isEmpty(collectTaskList)) {
            for (WalletCollectTask item : collectTaskList) {//循环获取手续费
                if (null == client) {
                    client = clientInfo.getClientFromId(item.getCoinId());
                }
                if (null != client) {
                    String txid = item.getTxid();
                    if (!StringUtils.isEmpty(txid)) {
                        BigDecimal feeBalance = client.getTransactionFee(txid).toBigDecimal();
                        walletCollectTaskService.updateById(item.setChainFee(feeBalance));
                    }
                }
            }
        } else {
            LOG.error("当前无需要查询的手续费");
        }
    }

}
