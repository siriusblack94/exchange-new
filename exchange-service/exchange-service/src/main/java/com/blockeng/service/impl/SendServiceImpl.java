package com.blockeng.service.impl;

import com.blockeng.dto.Sms;
import com.blockeng.enums.MessageChannel;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.service.SendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * @author qiang
 */
@Service
@Slf4j
public class SendServiceImpl implements SendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @Autowired
//    protected DisruptorTemplate disruptorTemplate;

    @Override
    public void sendTo(Sms sms) {


        try{
            //一条消息送rabbitMQ
            rabbitTemplate.convertSendAndReceive(MessageChannel.SMS_TAG.getChannel(), GsonUtil.toJson(sms));
            log.info("一条sms消息送rabbitMQ"+GsonUtil.toJson(sms));
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
//        String uuid = java.util.UUID.randomUUID().toString()|？“”;
//        DisruptorBindEvent event = new DisruptorBindEvent(sms, "message root
// jps
// " + uuid);
//
//        event.setEvent("Event-Output");
//        event.setTag("SendSms-Output");
//        event.setKey("id-" + uuid);
//        try{//一条信息送到这里
//            disruptorTemplate.publishEvent(event);
//            log.info("一条消息送event"+GsonUtil.toJson(sms));
//        }catch (Exception e){
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }

    }

    @Override
    public void sendTo(SimpleMailMessage email) {
        try{
            //一条消息送rabbitMQ
            rabbitTemplate.convertSendAndReceive(MessageChannel.MAIL_TAG.getChannel(), GsonUtil.toJson(email));
            log.info("一条mail消息送rabbitMQ"+GsonUtil.toJson(email));
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
//        String uuid = java.util.UUID.randomUUID().toString();
//        DisruptorBindEvent event = new DisruptorBindEvent(email, "message " + uuid);
//
//        event.setEvent("Event-Output");
//        event.setTag("SendMail-Output");
//        event.setKey("id-" + uuid);
//        log.info("event:----"+event);
//        try{
//            disruptorTemplate.publishEvent(event);
//        }catch (Exception e){
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }

    }
}