package com.blockeng.wallet.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.dto.WalletResultCode;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.mapper.CoinWithdrawMapper;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.blockeng.wallet.utils.GsonUtils;
import com.clg.wallet.enums.CoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
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
public class CoinWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinWithdrawService {

    private static final Logger LOG = LoggerFactory.getLogger(CoinWithdrawServiceImpl.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private CoinWithdrawService coinWithdrawService;


    /**
     * 跟新转账的状态
     *
     * @param item
     * @return
     */
    public boolean updateTx(CoinWithdraw item) {
        CoinWithdraw coinWithdraw = new CoinWithdraw().setId(item.getId()).setWalletMark(item.getWalletMark()).setStatus(item.getStatus()).setLastUpdateTime(new Date());
        if (!StringUtils.isEmpty(item.getTxid())) {
            coinWithdraw.setTxid(item.getTxid());
        }
        return super.insertOrUpdate(coinWithdraw);
    }


    public List<CoinWithdraw> queryOutList() {
        return queryOutList(null);
    }


    public List<CoinWithdraw> queryOutList(String type) {
        EntityWrapper<CoinWithdraw> ew = new EntityWrapper<>();
        ew.eq("status", 4);
        if (!StringUtils.isEmpty(type))
            ew.eq("coin_type", type);
        ew.andNew().isNull("txid").or().eq("txid", "");
        return super.selectList(ew);
    }


    public List<CoinWithdraw> queryNoFeeList(Long id, String type) {
        EntityWrapper<CoinWithdraw> ew = new EntityWrapper<>();

        if (null != id && id > 0) {
            ew.eq("coin_id", id);
        }
        if (!StringUtils.isEmpty(type)) {
            ew.eq("coin_type", type);
        }
        ew.andNew().isNull("chain_fee").or("chain_fee", "");
        ew.andNew().isNotNull("txid").ne("txid", "");
        return super.selectList(ew);
    }

    @Override
    public void updateDrawInfo(CoinWithdraw withdraw) {
        int status = withdraw.getStatus();
        if (status == Constant.PAY_SUCCESS_STATUS) {
            rabbitTemplate.convertAndSend(MessageChannel.COIN_WITHDRAW_MSG.getName(), WalletResultDTO.successResult(withdraw).toJson());
            LOG.info("打币成功,txid:" + withdraw.getTxid());
        } else {
            LOG.info("打币成功,coinId:" + withdraw.getId());
            rabbitTemplate.convertAndSend(MessageChannel.COIN_WITHDRAW_MSG.getName(), WalletResultDTO.errorResult(WalletResultCode.WITH_DRAW_FAILED.getCode(), withdraw).toJson());
        }
        coinWithdrawService.updateTx(withdraw);
    }

    @Override
    public void withDraw(String message, String type) {
        CoinWithdraw coinWithdraw = GsonUtils.convertObj(message, CoinWithdraw.class);
        if (null == coinWithdraw) {
            LOG.error("打币失败....");
            return;
        }
        try {
            if (CoinType.ETH.equalsIgnoreCase(type)) {//eth

            } else if (CoinType.ACT.equalsIgnoreCase(type)) {

            } else if (CoinType.BTC.equalsIgnoreCase(type)) {

            } else if (CoinType.ETH.equalsIgnoreCase(type)) {

            } else if (CoinType.ETC.equalsIgnoreCase(type)) {

            } else if (CoinType.NEO.equalsIgnoreCase(type)) {

            } else if (CoinType.WCG.equalsIgnoreCase(type)) {

            } else if (CoinType.XRP.equalsIgnoreCase(type)) {

            } else {

            }
        } catch (Exception e) {
            updateDrawInfo(coinWithdraw.setStatus(Constant.PAY_FAILED).setWalletMark("提币异常,请检查是否打币成功:" + e.getMessage()));
            e.printStackTrace();
        }

    }

}
