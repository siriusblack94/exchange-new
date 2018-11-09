package com.blockeng.wallet.bitcoin.handle;

import com.blockeng.wallet.bitcoin.bean.CoinWithdrawBean;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.entity.CoinWithdraw;
import com.blockeng.wallet.service.CoinWithdrawService;
import com.blockeng.wallet.service.UserAddressService;
import com.blockeng.wallet.utils.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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
     * 监听BTC提币消息
     */
    @RabbitListener(queues = {"finance.withdraw.send.btc"})
    public void coinWithdrawMessage(com.alibaba.fastjson.JSONObject message) {
        CoinWithdrawBean bean = GsonUtils.convertObj(GsonUtils.toJson(message), CoinWithdrawBean.class);
        if (null == bean) {
            log.error("btc打币失败,json转换异常");
//            return false;
        }
        CoinWithdraw result = coinWithdrawService.selectById(new CoinWithdraw().setId(bean.getId()));
        if (null != result && result.getStatus() == 5) {
            coinWithdrawService.updateDrawInfo(result.setStatus(Constant.PAY_SUCCESS_STATUS).setWalletMark("该笔提款已经打出,请勿重复发起提币操作"));
//            return true;
        }else {
            CoinWithdraw coinWithdraw = new CoinWithdraw();
            coinWithdraw.setId(bean.getId()).setAddress(bean.getAddress()).setCoinId(bean.getCoinId()).setCoinName(bean.getCoinName())
                    .setCoinType(bean.getCoinType()).setRemark("自动打币").setChainFee(bean.getChainFee()).setMum(bean.getMum())
                    .setNum(bean.getNum()).setFee(bean.getFee()).setStatus(bean.getStatus()).setType(bean.getType()).setUserId(bean.getUserId())
                    .setWalletMark(bean.getWalletMark());
            coinWithdrawService.insert(coinWithdraw);
        }

//        return true;

    }

    public  static void main(String args[]){
       String msg = "{\"mum\":4900.00000000,\"statusStr\":\"审核通过\",\"fee\":100.00000000,\"num\":5000.00000000,\"createdStr\":\"2018-08-21 21:58:14\",\"type\":1,\"blockNum\":0,\"id\":37,\"coinType\":\"btc\",\"idStr\":\"[37]\",\"address\":\"SgqKGWpnM9vYCY7HD7n4XNrrdZTg5TU54Y\",\"created\":1534859894000,\"userId\":1031398889066680321,\"coinId\":1031079466480603138,\"step\":2,\"coinName\":\"SGC\",\"lastUpdateTime\":1534859894000,\"status\":4}";
        CoinWithdrawBean coinWithdraw = GsonUtils.convertObj(msg, CoinWithdrawBean.class);
    }

//   /**
//     * 监听SGC提币消息
//     */
//    @RabbitListener(queues = {"plant.user.address"})
//    public String userAddress(String msg) {
//        try {
//            CoinAddressDTO coinAddress = GsonUtils.convertObj(msg, CoinAddressDTO.class);
//            return userAddressService.getAddress(coinAddress);
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
