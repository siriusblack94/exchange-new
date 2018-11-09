package com.blockeng.handle;

import com.blockeng.api.aliyuncs.MailConfig;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.repository.SendRecord;
import com.blockeng.repository.SendRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Slf4j
@Component
public class NewMailHandle {
    @Autowired
    private SendRecordRepository sendRecordRepository;

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    public JavaMailSender mailSender;

//    @RabbitListener(queues = {"touser.message.mail"})

    public void sendSMS(String msg) {
        SimpleMailMessage email = GsonUtil.convertObj(msg, SimpleMailMessage.class);

        log.info("rabbitMq 邮件监听 " + msg);
        try{
            email.setFrom(mailConfig.getAccount());
            log.info(mailConfig.getAccount());
            SendRecord sendRecord = new SendRecord();
            sendRecord.setEmail(Arrays.toString(email.getTo()));
            sendRecord.setContent(email.getText());
            sendRecord.setStatus(1);
            mailSender.send(email);
            sendRecord.setRemark("ok");
            sendRecordRepository.save(sendRecord);
        }catch(Exception e){
            e.printStackTrace();
        }
        log.info("发完了 " + msg);
    }

}
