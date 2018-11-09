//package com.blockeng.handlers;
//
//import com.blockeng.api.aliyuncs.MailConfig;
//import com.blockeng.repository.SendRecord;
//import com.blockeng.repository.SendRecordRepository;
//import com.lmax.disruptor.spring.boot.annotation.EventRule;
//import com.lmax.disruptor.spring.boot.event.DisruptorBindEvent;
//import com.lmax.disruptor.spring.boot.event.handler.DisruptorHandler;
//import com.lmax.disruptor.spring.boot.event.handler.chain.HandlerChain;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
///**
// * @author qiang
// */
//@EventRule("/Event-Output/SendMail-Output/**")
//@Component
//@Slf4j
//public class EmailDisruptorHandler implements DisruptorHandler<DisruptorBindEvent> {
//
//    @Autowired
//    private SendRecordRepository sendRecordRepository;
//
//    @Autowired
//    private MailConfig mailConfig;
//
//    @Autowired
//    public JavaMailSender mailSender;
//
//    @Override
//    public void doHandler(DisruptorBindEvent event, HandlerChain<DisruptorBindEvent> handlerChain) {
//        SimpleMailMessage email = (SimpleMailMessage) event.getSource();
//        email.setFrom(mailConfig.getAccount());
//
//        SendRecord sendRecord = new SendRecord();
//        sendRecord.setEmail(Arrays.toString(email.getTo()));
//        sendRecord.setContent(email.getText());
//        sendRecord.setStatus(1);
//        mailSender.send(email);
//        sendRecord.setRemark("ok");
//        sendRecordRepository.save(sendRecord);
//        if (log.isDebugEnabled()) {
//            log.debug(email.toString());
//        }
//    }
//}