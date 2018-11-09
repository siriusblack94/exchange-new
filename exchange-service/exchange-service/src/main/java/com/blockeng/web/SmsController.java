package com.blockeng.web;

import com.blockeng.service.SmsService;
import com.blockeng.vo.SendForm;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/sms")
@Slf4j
@Api(value = "发送短信", description = "发送短信 REST API")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/sendTo")
    void sendTo(@RequestBody SendForm form) {
        smsService.sendTo(form);
    }
}