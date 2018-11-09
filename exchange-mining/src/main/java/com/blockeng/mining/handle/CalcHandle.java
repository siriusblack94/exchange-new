package com.blockeng.mining.handle;

import com.blockeng.feign.ConfigServiceClient;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.mining.entity.TurnoverOrder;
import com.blockeng.mining.service.MiningDetailService;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @Description: 挖矿
 * @Author: Chen Long
 * @Date: Created in 2018/6/30 下午10:54
 * @Modified by: Chen Long
 */
@Component
@Slf4j
public class CalcHandle {


    @Autowired
    private MiningDetailService miningDetailService;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ConfigServiceClient configServiceClient;

    /**
     * 当用户资金清算完成之后,开始计算当前用户的当日交易总数据
     */
    @RabbitListener(queues = {"calc.account.tx.sum"})
    public void coinRechargeSuccess(String message) {
        try {
//            long startTime = System.currentTimeMillis();
//            log.info("挖矿开始:" + startTime);
            Boolean flag = true;
            TurnoverOrder turnoverOrder = GsonUtil.convertObj(message, TurnoverOrder.class);
            String mineCodeRewardSwitch = configServiceClient.getConfig("Mining", "MINE_CODE_REWARD_SWITCH").getValue().toUpperCase();
//            log.info("---info---orderid-" + turnoverOrder.getOrderId());
            Long coinId = Long.valueOf(configServiceClient.getConfig("Mining", "COIN_ID").getValue());
            if (null != turnoverOrder) {//开始处理
                //取消掉HB有关交易对的挖矿手续费返还
                if ("0".equals(mineCodeRewardSwitch)&&(turnoverOrder.getBuyCoinId().equals(coinId)||turnoverOrder.getSellCoinId().equals(coinId)))
                {
                    flag=false;
                  //  log.info("平台币不参与挖矿--开关--"+mineCodeRewardSwitch);
                }
                if (flag) {
//                    log.info("真实参与挖矿交易----"+turnoverOrder);
                    miningDetailService.calcTxInfo(turnoverOrder);
                }

            }
//            log.info("结束时间:" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {//当出现异常,吧错误信息提交到mogo上去
            log.error("资金清算异常" + e.getMessage());
            Document document = new Document();
            document.put("event_type", "tx.sum");
            document.put("msg", e.getMessage());
            document.put("event_data", message);
            mongoTemplate.getCollection("events").insertOne(document);
        }

    }
}
