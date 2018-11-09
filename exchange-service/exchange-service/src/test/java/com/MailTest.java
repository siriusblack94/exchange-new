package com;

import com.blockeng.api.aliyuncs.MailConfig;
import com.blockeng.framework.utils.GsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.test.context.junit4.SpringRunner;



@SpringBootTest(classes = {
        MailConfig.class, MailSenderAutoConfiguration.class
})
@RunWith(SpringRunner.class)
public class MailTest {

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    public JavaMailSender sender;

    @Test
    public void sendMailTest() {
     //   System.setProperty("java.net.preferIPv4Stack","true");
        SimpleMailMessage email = GsonUtil.convertObj(
                "{\"to\":[\"rw222222@126.com\"],\"subject\":\"用户注册验证\",\"text\":\"【BXX】您正在注册平台账号。验证码：739369，验证码有效时间：30分钟。请勿向任何人包括客服提供验证码！\"}"
                , SimpleMailMessage.class);
        email.setFrom(mailConfig.getAccount());
     //   javax.mail.Session session = javax.mail.Session.getDefaultInstance(new Properties(),new Authenticator(){
     //       @Override
     //       protected PasswordAuthentication getPasswordAuthentication(){
     //           return new PasswordAuthentication("rw222222@126.com","823420a");
     //       }
     //   });
      //  ((JavaMailSenderImpl) sender).setSession(session);
        sender.send(email);
    }


}
