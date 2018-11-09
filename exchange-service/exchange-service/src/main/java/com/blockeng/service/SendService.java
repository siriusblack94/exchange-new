package com.blockeng.service;

import com.blockeng.dto.Sms;
import org.springframework.mail.SimpleMailMessage;

/**
 * 短信和邮件发送
 *
 * @author qiang
 */
public interface SendService {

    /**
     * 发送短信
     *
     * @param sms
     */
    void sendTo(Sms sms);

    /**
     * 发送邮件
     *
     * @param email
     */
    void sendTo(SimpleMailMessage email);
}