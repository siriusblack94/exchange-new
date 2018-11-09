package com.blockeng.wallet.ethereum.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.ethereum.service.CoinEthWithdrawService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.CoinWithdrawMapper;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.clg.wallet.bean.EThTransactionBean;
import com.clg.wallet.bean.EthResult;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.help.WalletUtils;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.newclient.EthNewClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.bitcoinj.core.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Transactional
@Slf4j
public class CoinEthWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinEthWithdrawService {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinWithdrawService coinWithdrawService;


    /**
     * 打eth或者ethToken
     *
     * @param item
     */
    private void sendEthOrToken(CoinWithdraw item) throws Exception {
        String addr = item.getAddress();
        if (StringUtils.isEmpty(addr) || !WalletUtils.isValidAddress(addr)) {
            item.setStatus(Constant.PAY_FAILED_REJECT).setWalletMark("地址格式错误");
            log.info("地址格式不对");
            coinWithdrawService.updateDrawInfo(item);
            return;
        }
        log.error("打币类型--------------------------：" + item.getCoinType());
        log.error("币种类型-----eth------------------：" + CoinType.ETH);
        log.error("币种类型-----ethtoken-------------：" + CoinType.ETHTOKEN);
        if (CoinType.ETH.equalsIgnoreCase(item.getCoinType())) {//打币eth
            sendEth(item);
        } else if (CoinType.ETHTOKEN.equalsIgnoreCase(item.getCoinType())) {//打币ethtoken
            sendEthToken(item);
        }
    }

    @Override
    public void transaction() {
        List<CoinWithdraw> coinWithdraws = coinWithdrawService.queryOutList(CoinType.ETH);
        coinWithdraws.addAll(coinWithdrawService.queryOutList(CoinType.ETHTOKEN));
        if (CollectionUtils.isEmpty(coinWithdraws)) {
            log.error("当前无提币信息");
            return;
        }
        //增加事务控制，防止以太坊打币确认返回过长，导致的重复打币
        for(CoinWithdraw cw:coinWithdraws){
            coinWithdrawService.updateTx(cw.setStatus(Constant.PAY_TEMP).setWalletMark("打币中，请耐心等待，切勿重复处理"));
        }
        log.error("------------提币信息个数：" + coinWithdraws.size());
        for (CoinWithdraw item : coinWithdraws) {
            try {
                sendEthOrToken(item);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                coinWithdrawService.updateDrawInfo(item.setStatus(Constant.PAY_FAILED).setWalletMark("提币异常,请检查是否打币成功:" + e.getMessage()));
            }
        }

    }


    /**
     * @param item 打币eth
     * @throws Exception
     */
    private void sendEthToken(CoinWithdraw item) throws Exception {
        log.info("初始化打币连接：" + item.getCoinId());
        CoinConfig coin = clientInfo.getCoinConfigFormId(item.getCoinId());
        log.info("初始化打币连接：" + CoinType.ETH);
        EthNewClient client = (EthNewClient) ClientFactory.getClient(clientInfo.getClientInfoFromType(CoinType.ETH));
        EThTransactionBean ethTransactionBean = clientInfo.getEthTransactionBean(coin, item);

        log.info("开始打币--" + coin.getName());
        log.info("开始打币 合约地址--" + ethTransactionBean.getContractAddress());
        log.info("开始打币 转出地址--" + ethTransactionBean.getFromAddress());
        BigDecimal tokenBalance = client.getTokenBalance(ethTransactionBean.getContractAddress(), ethTransactionBean.getFromAddress()).toBigDecimal();
        log.info("判断余额" + tokenBalance);
        if (tokenBalance.compareTo(item.getMum()) > 0) {
            coinWithdrawService.updateTx(item.setTxid(Constant.MOCK_TXID).setWalletMark(""));
            EthResult ethTokenResult = client.sentEthToken(ethTransactionBean);
            if (null != ethTokenResult && ethTokenResult.isSuccess()) {
                item.setTxid(ethTokenResult.getTxid()).setWalletMark(ethTokenResult.getInfo()).setStatus(Constant.PAY_SUCCESS_STATUS);
            } else {
                item.setTxid(ethTokenResult.getTxid()).setWalletMark(ethTokenResult.getInfo()).setStatus(Constant.PAY_FAILED);
                log.info("打币失败,id:" + item.getId());
            }
        } else {
            coinWithdrawService.updateTx(item.setStatus(Constant.PAY_FAILED).setWalletMark("余额不足"));
        }
        coinWithdrawService.updateDrawInfo(item);

    }


    /**
     * @param item 打币ethToken
     * @throws Exception
     */
    private void sendEth(CoinWithdraw item) throws Exception {
        CoinConfig coin = clientInfo.getCoinConfigFormId(item.getCoinId());
        EthNewClient client = (EthNewClient) ClientFactory.getClient(clientInfo.getClientInfoFromType(CoinType.ETH));
        EThTransactionBean ethTransactionBean = clientInfo.getEthTransactionBean(coin, item);

        log.info("开始打币" + coin.getName());
        BigDecimal ethBalance = client.getBalance(ethTransactionBean.getFromAddress()).toBigDecimal();
        if (ethBalance.compareTo(item.getMum()) == 1) {
            coinWithdrawService.updateTx(item.setTxid(Constant.MOCK_TXID).setWalletMark(""));
            EthResult ethResult = client.sentEth(ethTransactionBean);
            if (null != ethResult && ethResult.isSuccess()) {
                item.setTxid(ethResult.getTxid()).setWalletMark(ethResult.getInfo()).setStatus(Constant.PAY_SUCCESS_STATUS);
            } else {
                item.setTxid(ethResult.getTxid()).setWalletMark(ethResult.getInfo()).setStatus(Constant.PAY_FAILED);
                log.info("打币失败,id:" + item.getId());
            }
        } else {
            coinWithdrawService.updateTx(item.setStatus(Constant.PAY_FAILED).setWalletMark("余额不足"));
        }
        coinWithdrawService.updateDrawInfo(item);
    }
}
