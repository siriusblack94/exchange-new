package com.blockeng.wallet.service.impl;

import com.blockeng.wallet.entity.*;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.blockeng.wallet.service.WalletCollectTaskService;
import com.clg.wallet.bean.ClientBean;
import com.blockeng.wallet.config.Constant;
import com.clg.wallet.bean.ResultDTO;
import com.clg.wallet.enums.CoinType;
import com.blockeng.wallet.help.ClientInfo;
import com.clg.wallet.enums.ResultCode;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import com.blockeng.wallet.service.CoinRechargeService;
import com.blockeng.wallet.service.CommitStatusService;
import com.clg.wallet.wallet.act.AchainCore;
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
import java.util.NoSuchElementException;

@Transactional
@Service
public class CommitStatusServiceImpl implements CommitStatusService {


    private static final Logger LOG = LoggerFactory.getLogger(CommitStatusServiceImpl.class);


    @Autowired
    private ClientInfo clientInfo;


    @Autowired
    private CoinRechargeService coinRechargeService;


    @Autowired
    private CoinWithdrawService coinWithdrawService;


    @Autowired
    private WalletCollectTaskService walletCollectTaskService;

    private Client client;

    public void commitEth() {
        updateEthOrEtc(CoinType.ETH);
    }

    @Override
    public void commitEtc() {
        updateEthOrEtc(CoinType.ETC);
    }


    private void updateEthOrEtc(String type) {
        Client client = clientInfo.getClientFromType(type);

        //获取eth和ethToken的打币信息
        List<CoinWithdraw> withdrawList = coinWithdrawService.queryNoFeeList(0L, type);
        if (CoinType.ETH.equalsIgnoreCase(type))
            withdrawList.addAll(coinWithdrawService.queryNoFeeList(0L, CoinType.ETHTOKEN));


        if (!CollectionUtils.isEmpty(withdrawList)) {
            for (CoinWithdraw withdraw : withdrawList) {
                String txid = withdraw.getTxid();
                if (!StringUtils.isEmpty(txid)) {
                    if (!txid.equalsIgnoreCase(Constant.MOCK_TXID)) {//check时间,如果时间太长,超过了一定的时间,直接删除该数据
                        try {
                            ResultDTO resultDTO = client.getTransactionFee(txid);
                            if (resultDTO.getStatusCode() == ResultCode.SUCCESS.getCode()) { //正在打包中,等待
                                BigDecimal fee = resultDTO.toBigDecimal();
                                coinWithdrawService.updateById(withdraw.setChainFee(fee));
                            }
                        } catch (NoSuchElementException e) {
                            e.printStackTrace();
                            LOG.error("没有找到相应的充值记录,txid:" + txid);
                            continue;
                        } catch (Exception e) {
                            LOG.error("未查询到相应的区块信息,txid:" + txid);
                            e.printStackTrace();
                            continue;
                        }
                    }
                }
            }
        }

        //获取eth和ethToken的归账信息的手续费
        List<WalletCollectTask> collectTask = walletCollectTaskService.queryNoFeeList(0L, type);
        if (CoinType.ETH.equalsIgnoreCase(type))
            collectTask.addAll(walletCollectTaskService.queryNoFeeList(0L, CoinType.ETHTOKEN));

        if (!CollectionUtils.isEmpty(collectTask)) {
            for (WalletCollectTask walletCollectTask : collectTask) {
                String txid = walletCollectTask.getTxid();
                if (!StringUtils.isEmpty(txid)) {
                    ResultDTO resultDTO = client.getTransactionFee(txid);
                    if (resultDTO.getStatusCode() == ResultCode.SUCCESS.getCode()) {
                        BigDecimal fee = resultDTO.toBigDecimal();
                        walletCollectTaskService.updateById(walletCollectTask.setChainFee(fee));
                    }
                }
            }
        }
    }


    @Override
    public void commitAct() {
        List<CoinRecharge> rechargeList = getRechargeList(CoinType.ACT, CoinType.ACTTOKEN);
        if (!CollectionUtils.isEmpty(rechargeList)) {
            for (CoinRecharge recharge : rechargeList) {
                CoinConfig coin = clientInfo.getCoinConfigFormId(recharge.getCoinId());
                commitStatus(recharge, coin);
            }
        }
    }

    @Override
    public void commitBtc() {
        List<CoinWithdraw> withdrawList = coinWithdrawService.queryNoFeeList(0L, CoinType.BTC);
        if (!CollectionUtils.isEmpty(withdrawList)) { //不等于null
            for (CoinWithdraw withDraw : withdrawList) { //获取手续费


            }
        }

        List<CoinRecharge> rechargeList = getRechargeList(CoinType.BTC);
        if (!CollectionUtils.isEmpty(rechargeList)) {
            for (CoinRecharge recharge : rechargeList) {
                CoinConfig coin = clientInfo.getCoinConfigFormId(recharge.getCoinId());
                commitStatus(recharge, coin);
            }
        }
    }


    public void commitNeo() {
        List<CoinRecharge> rechargeList = getRechargeList(CoinType.NEO, CoinType.NEOTOKEN);
        if (!CollectionUtils.isEmpty(rechargeList)) {
            for (CoinRecharge recharge : rechargeList) {
                CoinConfig coin = clientInfo.getCoinConfigFormId(recharge.getCoinId());
                commitStatus(recharge, coin);
            }
        }
    }


    /**
     * 确认到账服务
     *
     * @param userInWallet
     * @param coin
     */

    private void commitStatus(CoinRecharge userInWallet, CoinConfig coin) {
        client = ClientFactory.getClient(ClientBeanMapper.INSTANCE.form(coin));
        int commit = client.getTransactionConfirmed(userInWallet.getTxid()).toInteger();
        if (AchainCore.BLOCK_NOT_FOUNT == commit) {//充值失败
            coinRechargeService.updateInsertWallet(userInWallet.getId(), -1);//-1充值失败
        } else {
            checkIsSuccess(commit, userInWallet, coin);
        }
    }


    /**
     * 检测是否充值成功
     *
     * @param commit
     * @param userInWallet
     * @param coin
     */
    private void checkIsSuccess(int commit, CoinRecharge userInWallet, CoinConfig coin) {

        int needConfirmed = coin.getMinConfirm() - commit;
        if (needConfirmed > 0) {//更改确认数
            coinRechargeService.updateInsertWalletCommit(userInWallet.getId(), -needConfirmed);//-1充值失败
        } else {//充值
            if (CoinType.ETH.equalsIgnoreCase(coin.getCoinType()) ||
                    CoinType.ETHTOKEN.equalsIgnoreCase(coin.getCoinType())) { //是eth需要再次校验
                boolean failed = ((EthNewClient) client).isFailed(userInWallet.getTxid()).toBoolean();
                if (failed) {
                    coinRechargeService.updateInsertWallet(userInWallet.getId(), -1);//充值失败
                    return;
                }
            }
            coinRechargeService.updateInsertWalletCommit(userInWallet.getId(), Constant.INSERT_SUCCESS);//充值成功

            if (CoinType.ETH.equalsIgnoreCase(coin.getCoinType())
                    || CoinType.ETHTOKEN.equalsIgnoreCase(coin.getCoinType())) {//如果是eth或者eth的代币需要归账
                addTask(userInWallet, CoinType.ETH, coin.getCoinType());
            } else if (CoinType.ETC.equalsIgnoreCase(coin.getCoinType())) {//如果是etc,也需要归账
                addTask(userInWallet, CoinType.ETC, coin.getCoinType());
            } else if (CoinType.BTC.equalsIgnoreCase(coin.getCoinType())) {
                //TODO default归账
            } else if (CoinType.NEO.equalsIgnoreCase(coin.getCoinType())) {

            } else if (CoinType.XRP.equalsIgnoreCase(coin.getCoinType())) {

            }

        }
    }

    private void addTask(CoinRecharge userInWallet, String coinTypeEtc, String type) {
        ClientBean clientBean = clientInfo.getClientInfoFromType(coinTypeEtc);
        if (coinRechargeService.addCollectTask(clientBean, userInWallet, type)) {//增加归集任务
            LOG.info("增加归集任务成功");
        } else {
            LOG.info("增加归集任务失败");

        }
    }


    private List<CoinRecharge> getRechargeList(String type) {
        return getRechargeList(type, null);
    }

    private List<CoinRecharge> getRechargeList(String type, String typeToken) {
        if (!StringUtils.isEmpty(type)) {
            List<CoinRecharge> list = coinRechargeService.getNotDealInWalletByType(type);
            if (!StringUtils.isEmpty(typeToken)) {
                List<CoinRecharge> tokenList = coinRechargeService.getNotDealInWalletByType(typeToken);
                list.addAll(tokenList);
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }

}
