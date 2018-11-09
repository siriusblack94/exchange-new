package com.blockeng.handle;

import com.blockeng.bean.CoinWithdrawBean;
import com.blockeng.bean.CoinWithdrawModel;
import com.blockeng.dto.SendForm;
import com.blockeng.entity.CoinWithdraw;
import com.blockeng.feign.SmsServiceClient;
import com.blockeng.framework.enums.BusinessType;
import com.blockeng.framework.enums.CoinWithdrawStatus;
import com.blockeng.framework.enums.SmsTemplate;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.service.AccountService;
import com.blockeng.service.CoinWithdrawService;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 数字货币提现打币成功
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 下午10:54
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class CoinWithdrawHandle {

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private SmsServiceClient smsServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 数字货币提现打币成功
     *
     * @param message 提币消息
     */
    @RabbitListener(queues = {"finance.withdraw.result"})
    public void coinRechargeSuccess(String message) {
        if (StringUtils.isEmpty(message)) {
            log.error("数字货币提现结果消息为空");
            return;
        }
        log.info("结果消息-----"+message);
        CoinWithdrawBean coinWithdrawBean ;
        try {
            coinWithdrawBean = GsonUtil.convertObj(message, CoinWithdrawBean.class);
            if(coinWithdrawBean==null){
                log.error("提现申请单转换错误1");
            }
            CoinWithdrawModel model = coinWithdrawBean.getResult();
            if(coinWithdrawBean.getResult()==null){
                log.error("提现申请单转换错误2");
            }

            if (null != model) {
                CoinWithdraw coinWithdraw = coinWithdrawService.selectById(model.getId());
                if (coinWithdraw == null) {
                    log.error("提现申请单ID错误");
                    return;
                }
                if (model.getStatus() == 5) {//打币成
                    // 更新提币申请单状态
                    coinWithdraw.setStatus(CoinWithdrawStatus.SUCCESS.getCode());
                    coinWithdraw.setTxid(model.getTxid());
                    coinWithdraw.setFee(model.getFee());
                    coinWithdrawService.updateById(coinWithdraw);
                    // 扣减账户资金
                    accountService.subtractAmount(coinWithdraw.getUserId(),
                            coinWithdraw.getCoinId(),
                            coinWithdraw.getNum(),
                            BusinessType.WITHDRAW,
                            BusinessType.WITHDRAW.getDesc(),
                            coinWithdraw.getId());

                    // 短信通知用户
                    UserDTO user = userServiceClient.selectById(coinWithdraw.getUserId());
                    Map<String, Object> templateParam = new HashMap<>();
                    templateParam.put("amount", coinWithdraw.getMum());
                    templateParam.put("coinName", coinWithdraw.getCoinName());

                    SendForm sendForm = new SendForm();
                    sendForm.setCountryCode(user.getCountryCode())
                            .setMobile(user.getMobile())
                            .setTemplateCode(SmsTemplate.WITHDRAW_SUCCESS.getCode())
                            .setEmail(user.getEmail())
                            .setTemplateParam(templateParam);
                    smsServiceClient.sendTo(sendForm);
                } else {
                    if (model.getStatus() == CoinWithdrawStatus.REFUSE.getCode()) {
                        // 提币失败
                        coinWithdraw.setStatus(CoinWithdrawStatus.REFUSE.getCode())
                                .setWalletMark(model.getWalletMark());
                        coinWithdrawService.updateById(coinWithdraw);
                        // 解冻资金
                        accountService.unlockAmount(coinWithdraw.getUserId(),
                                coinWithdraw.getCoinId(),
                                coinWithdraw.getNum(),
                                BusinessType.WITHDRAW,
                                coinWithdraw.getId());
                    } else {
                        // 打币失败
                        coinWithdraw.setStatus(CoinWithdrawStatus.FAILED.getCode())
                                .setWalletMark(model.getWalletMark());
                        coinWithdrawService.updateById(coinWithdraw);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            log.error("数字货币提现结果处理失败");
        }
    }

    public  static void main(String args[]){
        String message = " {\"statusCode\":1,\"resultDesc\":\"SUCCESS\",\"result\":{\"id\":55,\"userId\":1034836121275748353,\"coinId\":1034818470040944642,\"coinName\":\"ETH\",\"coinType\":\"eth\",\"address\":\"0x57184A7F1f189f36c2117f61A976A5d2f8671E6F\",\"status\":5,\"txid\":\"0x192cab4582284f4bdbd53cf31f56a8d257fd701bf397ed932ac2d320208fe279\",\"num\":0.02000000,\"fee\":0.01000000,\"mum\":0.01000000,\"type\":1,\"blockNum\":0,\"remark\":\"自动打币\",\"walletMark\":\"打币成功\",\"lastUpdateTime\":\"Aug 30, 2018 12:59:50 PM\",\"created\":\"Aug 30, 2018 12:59:50 PM\"}}";
        CoinWithdrawBean coinWithdrawBean ;
        coinWithdrawBean = GsonUtil.convertObj(message, CoinWithdrawBean.class);
        CoinWithdrawModel coinWithdrawResult = coinWithdrawBean.getResult();
    }
}
