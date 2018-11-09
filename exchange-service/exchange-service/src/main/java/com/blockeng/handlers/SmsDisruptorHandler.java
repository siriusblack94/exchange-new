//package com.blockeng.handlers;
//
//import com.blockeng.api.SmsApi;
//import com.blockeng.dto.Sms;
//import com.blockeng.repository.SendRecord;
//import com.blockeng.repository.SendRecordRepository;
//import com.blockeng.vo.mappers.SendRecordMapper;
//import com.lmax.disruptor.spring.boot.annotation.EventRule;
//import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
//import com.lmax.disruptor.spring.boot.event.handler.DisruptorHandler;
//import com.lmax.disruptor.spring.boot.event.handler.chain.HandlerChain;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * @author qiang
// */
//@EventRule("/Event-Output/SendSms-Output/**")
//@Component
//@Slf4j
//public class SmsDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {
//
//    @Autowired
//    private SendRecordRepository sendRecordRepository;
//
//    @Override
//    public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) {
//        log.info("短信的，监听到了吗？-1-" + event.getKey());
//        log.info("短信的，监听到了吗？-2-" + event.getSource());
//        Sms sms = (Sms) event.getSource();
//        log.info("短信的，监听到了吗？-3-" + event.getSource());
//        SendRecord sendRecord = SendRecordMapper.INSTANCE.map(sms);
//        log.info("短信的，监听到了吗？-4-" + sendRecord);
//        log.info("短信的，监听到了吗？-4-" + sendRecord.getStatus());
//        String result = SmsApi.sendTo(sms.getCountryCode(), sms.getPhone(), sms.getContent());
//        sendRecord.setStatus(1);
//        Date date = new Date();
//        sendRecord.setCreated(date);
//        sendRecord.setLastUpdateTime(date);
//        log.info("短信的，监听到了吗？-5-" + sendRecord.getStatus());
//        sendRecord.setRemark(result);
//        log.info("短信的，监听到了吗？-6-保存数据库！ 怎么保存的？");
//        sendRecordRepository.save(sendRecord);
//        log.info("短信的，监听到了吗？-7-保存数据库！ 执行结束了！！！！！！");
//        if (log.isDebugEnabled()) {
//            log.debug(sms.toString());
//        }
//    }
//}