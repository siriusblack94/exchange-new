package com.blockeng.handle;

import com.blockeng.api.SmsApi;
import com.blockeng.dto.Sms;
import com.blockeng.framework.utils.GsonUtil;
import com.blockeng.repository.SendRecord;
import com.blockeng.repository.SendRecordRepository;
import com.blockeng.vo.mappers.SendRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class NewSmsHandle {
    @Autowired
    private SendRecordRepository sendRecordRepository;


    @RabbitListener(queues = {"touser.message.sms"})
    public void sendSMS(String msg) {
        Sms sms = GsonUtil.convertObj(msg, Sms.class);

        log.info("rabbitMq 短信监听 " + msg);

         SendRecord sendRecord = SendRecordMapper.INSTANCE.map(sms);
         String result = SmsApi.sendTo(sms.getCountryCode(), sms.getPhone(), sms.getContent());
         sendRecord.setStatus(1);
         Date date = new Date();
         sendRecord.setCreated(date);
         sendRecord.setLastUpdateTime(date);
         sendRecord.setRemark(result);
         sendRecordRepository.save(sendRecord);

        log.info("短信发送完毕 " + msg);
    }

}
