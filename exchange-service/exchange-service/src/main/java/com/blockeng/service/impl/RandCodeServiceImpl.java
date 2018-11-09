package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.RandCode;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.dto.Sms;
import com.blockeng.entity.Config;
import com.blockeng.service.ConfigService;
import com.blockeng.service.RandCodeService;
import com.blockeng.service.SendService;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author qiang
 */
@Service
@Slf4j
public class RandCodeServiceImpl implements RandCodeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ConfigService configService;

    @Autowired
    private SendService sendService;

    @Override
    public void sendTo(RandCode randCode) {
        String code = RandomStringUtils.randomNumeric(6);
        QueryWrapper<Config> ew = new QueryWrapper<>();
        ew.eq("type", "SMS");
        ew.eq("code", randCode.getTemplateCode());
        log.info("type:sms");
        log.info("code:" + randCode.getTemplateCode());
        Config config = configService.selectOne(ew);
        if (Optional.ofNullable(config).isPresent()) {
            String content = config.getValue();
            QueryWrapper<Config> signQueryWrapper = new QueryWrapper<>();
            signQueryWrapper.eq("type", "SMS");
            signQueryWrapper.eq("code", "SIGN");
            Config signConfig = configService.selectOne(signQueryWrapper);
            if (Optional.ofNullable(signConfig).isPresent()) {
                content = content.replaceAll("\\$\\{".concat("sign").concat("\\}"), signConfig.getValue());
            }
            content = content.replaceAll("\\$\\{".concat("code").concat("\\}"), code);
            log.info("获得签名:" + content);
            if (!Strings.isNullOrEmpty(randCode.getPhone())) {
                log.info("设置短信验证码缓存到redis: start");
                String key = String.format("CAPTCHA:%s:%s", randCode.getTemplateCode(), randCode.getCountryCode() + randCode.getPhone());
                stringRedisTemplate.opsForValue().set(key, code);
                stringRedisTemplate.expire(key, 30L, TimeUnit.MINUTES);
                log.info("设置短信验证码缓存到redis: end");
                Sms sms = new Sms();
                sms.setCountryCode(randCode.getCountryCode());
                sms.setPhone(randCode.getPhone());
                sms.setContent(content);
                log.info("准备发送--" + sms.getCountryCode() + "---" + sms.getCountryCode() + "--" + sms.getContent());
                sendService.sendTo(sms);
            } else if (!Strings.isNullOrEmpty(randCode.getEmail())) {
                String key = String.format("CAPTCHA:%s:%s", randCode.getTemplateCode(), randCode.getEmail());
                stringRedisTemplate.opsForValue().set(key, code);
                stringRedisTemplate.expire(key, 30L, TimeUnit.MINUTES);

                String to = randCode.getEmail();
                String subject = config.getName();
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(content);
                sendService.sendTo(message);
            }
        } else {
            throw new RuntimeException("短信模板不存在！");
        }
    }

    @Override
    public boolean verify(RandCodeVerifyDTO randCodeVerifyDTO) {
        String account = randCodeVerifyDTO.getEmail();
        if (!Strings.isNullOrEmpty(randCodeVerifyDTO.getPhone())) {
            account = randCodeVerifyDTO.getCountryCode() + randCodeVerifyDTO.getPhone();
        }
        String key = String.format("CAPTCHA:%s:%s", randCodeVerifyDTO.getTemplateCode(), account);
        String value = stringRedisTemplate.opsForValue().get(key);
        if (!Strings.isNullOrEmpty(account) && !Strings.isNullOrEmpty(value) && value.equals(randCodeVerifyDTO.getCode())) {
            stringRedisTemplate.delete(key);
            return true;
        } else {
            return false;
        }
    }
}