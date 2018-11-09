package com.blockeng.wallet.bitcoin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.bitcoin.service.CoinBtcWithdrawService;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.mapper.CoinWithdrawMapper;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.bean.TxData;
import com.clg.wallet.enums.CoinType;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.clg.wallet.utils.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
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
public class CoinBtcWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinBtcWithdrawService {

    @Autowired
    private ClientInfo clientInfo;

    @Autowired
    private CoinWithdrawService coinWithdrawService;


    /**
     * 打币
     *
     * @param coinWithdraw
     */
    private void sendDefault(CoinWithdraw coinWithdraw) {
        ClientBean coin = clientInfo.getClientInfoFromId(coinWithdraw.getCoinId());
        Client client = ClientFactory.getClient(coin);
        BigDecimal balance = client.getBalance().toBigDecimal();
        if (balance.compareTo(coinWithdraw.getMum()) < 0) {
            coinWithdraw.setStatus(Constant.PAY_FAILED).setWalletMark("余额不足");
            coinWithdrawService.updateDrawInfo(coinWithdraw);
            return;
        }
        log.error("--开始打币-------");
        String txid = client.sendNormalOut(new TxData().
                setToAddress(coinWithdraw.getAddress()).
                setPass(coin.getWalletPassOut()).
                setBalance(BigDecimalUtils.formatBigDecimal(coinWithdraw.getMum()))).getResult().toString();
        log.error("--打币完毕-------"+txid);
        if (!StringUtils.isEmpty(txid)) {
            BigDecimal fee = client.getTransactionFeeOut(txid).toBigDecimal();
            coinWithdraw.setTxid(txid).setStatus(Constant.PAY_SUCCESS_STATUS).setWalletMark("提币成功").setChainFee(fee);
        } else {
            coinWithdraw.setStatus(Constant.PAY_FAILED).setWalletMark("打币失败,未知异常");
        }
        coinWithdrawService.updateDrawInfo(coinWithdraw);
    }

    @Override
    public void transaction() {
        List<CoinWithdraw> coinWithdraws = coinWithdrawService.queryOutList(CoinType.BTC);
        List<CoinConfig> coinList = clientInfo.getCoinConfigFormType(CoinType.BTC);

        if (CollectionUtils.isEmpty(coinWithdraws)) {
            log.error("当前无提币信息");
            return;
        }
        for (CoinWithdraw item : coinWithdraws) {
            log.error("coinname:"+item.getCoinName()+"--cointype:"+item.getCoinType());
            boolean auto = false;
            for(CoinConfig coin:coinList){
                if(coin.getCoinType().equals(item.getCoinType())&&coin.getAutoDraw()==1){
                    auto = true;
                    break;
                }
            }
            if(auto){
                try {
                    sendDefault(item);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("------异常----");
                    coinWithdrawService.updateDrawInfo(item.setStatus(Constant.PAY_FAILED).setWalletMark("提币异常,请检查是否打币成功:" + e.getMessage()));
                }
            }
        }

    }
}
