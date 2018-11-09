package com.blockeng.api.aliyuncs;

import com.blockeng.framework.utils.GsonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "mail")
@Configuration
public class MailConfig {

    private String account;

}
