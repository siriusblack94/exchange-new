package com.blockeng.mailtest;

import com.blockeng.framework.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//public class MainServer implements ApplicationListener<ContextRefreshedEvent> {
@Slf4j
public class MainServer {
    @Autowired
    MailConfig mailConfig;

    @Autowired
    JavaMailSender sender;


    @RabbitListener(queues = {"touser.message.mail"})
    public void sendSMS(String msg) {
        SimpleMailMessage email = GsonUtil.convertObj(msg, SimpleMailMessage.class);

        log.info("rabbitMq 邮件监听 " + msg);
        try {
            email.setTo(email.getTo()[0]);
            email.setFrom(mailConfig.getAccount());
            log.info(mailConfig.getAccount());
            sender.send(email);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("", e);
        }
        log.info("发完了 " + msg);
    }

    @GetMapping("/send")
    public String send(String json) {
        String resp = "1";
        try {
            SimpleMailMessage email = GsonUtil.convertObj(json
//                    "{\"to\":[\"rw222222@126.com\"],\"subject\":\"用户注册验证\",\"text\":\"【BXX】您正在注册平台账号。验证码：739369，验证码有效时间：30分钟。请勿向任何人包括客服提供验证码！\"}"
                    , SimpleMailMessage.class);
            email.setFrom(mailConfig.getAccount());
            sender.send(email);
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getCause().getMessage();
        }
        System.out.println("over");
        return resp;
    }
//
//    volatile boolean loaded ;
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        if (event instanceof ContextRefreshedEvent && ((ContextRefreshedEvent) event).getApplicationContext().getParent() == null && !loaded) {
//            send();
//            loaded = true;
//        }
//
//    }


}
