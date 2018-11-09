package com.blockeng.wallet.ethereum.handle;

import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.dto.CoinAddressDTO;
import com.blockeng.wallet.dto.WalletResultCode;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.enums.MessageChannel;
import com.blockeng.wallet.ethereum.bean.CoinWithdrawBean;
import com.blockeng.wallet.ethereum.service.CoinEthWithdrawService;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.blockeng.wallet.service.UserAddressService;
import com.blockeng.wallet.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 提币消息处理
 * @Author: Chen Long
 * @Date: Created in 2018/6/24 下午4:55
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class CoinWithdrawHandle {


    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    /**
     * 监听ETH提币消息
     */
    @RabbitListener(queues = {"finance.withdraw.send.eth"})
    public void coinWithdrawMessage(com.alibaba.fastjson.JSONObject message) {
        log.error("eth提币消息:----" + message);
        CoinWithdrawBean bean = GsonUtils.convertObj(GsonUtils.toJson(message), CoinWithdrawBean.class);
        if (null == bean) {
            log.error("eth打币失败,json转换异常");
        }
        CoinWithdraw result = coinWithdrawService.selectById(new CoinWithdraw().setId(bean.getId()));
        if (null != result && result.getStatus() == 5) {
            coinWithdrawService.updateDrawInfo(result.setStatus(Constant.PAY_SUCCESS_STATUS).setWalletMark("该笔提款已经打出,请勿重复发起提币操作"));
        }else {
            CoinWithdraw coinWithdraw = new CoinWithdraw();
            coinWithdraw.setId(bean.getId()).setAddress(bean.getAddress()).setCoinId(bean.getCoinId()).setCoinName(bean.getCoinName())
                    .setCoinType(bean.getCoinType()).setRemark("自动打币").setChainFee(bean.getChainFee()).setMum(bean.getMum())
                    .setNum(bean.getNum()).setFee(bean.getFee()).setStatus(bean.getStatus()).setType(bean.getType()).setUserId(bean.getUserId())
                    .setWalletMark(bean.getWalletMark());
            coinWithdrawService.insert(coinWithdraw);
        }
    }


    /**
     * 监听XRP提币消息
     */
    @RabbitListener(queues = {"plant.user.address"})
    public String userAddress(String msg) {
        try {
            CoinAddressDTO coinAddress = GsonUtils.convertObj(msg, CoinAddressDTO.class);
            log.info("event  userId:[" + coinAddress.getUserId() + "]" + "-----coinid:[" + coinAddress.getCoinId() + "]");
            return userAddressService.getAddress(coinAddress);
        } catch (Exception e) {
            return null;
        }
    }
}
