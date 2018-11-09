package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.Sms;
import com.blockeng.entity.Config;
import com.blockeng.service.ConfigService;
import com.blockeng.service.SendService;
import com.blockeng.service.SmsService;
import com.blockeng.vo.SendForm;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 短信信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Autowired
    private ConfigService configService;

    @Autowired
    private SendService sendService;

    @Override
    public void sendTo(SendForm form) {
        QueryWrapper<Config> ew = new QueryWrapper<>();
        ew.eq("type", "SMS");
        ew.eq("code", form.getTemplateCode());
        Config config = configService.selectOne(ew);
        if (Optional.ofNullable(config).isPresent()) {
            if (!Strings.isNullOrEmpty(form.getMobile())) {
                QueryWrapper<Config> signQueryWrapper = new QueryWrapper<>();
                signQueryWrapper.eq("type", "SMS");
                signQueryWrapper.eq("code", "SIGN");
                Config signConfig = configService.selectOne(signQueryWrapper);
                Map<String, Object> templateParam = form.getTemplateParam();
                templateParam.put("sign", signConfig.getValue());
                String content = config.getValue();
                for (String s : templateParam.keySet()) {
                    content = content.replaceAll("\\$\\{".concat(s).concat("\\}")
                            , templateParam.get(s).toString());
                }

                Sms sms = new Sms();
                sms.setCountryCode(form.getCountryCode());
                sms.setPhone(form.getMobile());
                sms.setContent(content);
                sendService.sendTo(sms);
            } else if (!Strings.isNullOrEmpty(form.getEmail())) {
                QueryWrapper<Config> signQueryWrapper = new QueryWrapper<>();
                signQueryWrapper.eq("type", "SMS");
                signQueryWrapper.eq("code", "SIGN");
                Config signConfig = configService.selectOne(signQueryWrapper);
                Map<String, Object> templateParam = form.getTemplateParam();
                templateParam.put("sign", signConfig.getValue());
                String content = config.getValue();
                for (String s : templateParam.keySet()) {
                    content = content.replaceAll("\\$\\{".concat(s).concat("\\}")
                            , templateParam.get(s).toString());
                }

                SimpleMailMessage message = new SimpleMailMessage();
                String to = form.getEmail();
                String subject = config.getName();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);
                sendService.sendTo(message);
            }
        } else {
            throw new RuntimeException("模板不存在！");
        }
    }
}