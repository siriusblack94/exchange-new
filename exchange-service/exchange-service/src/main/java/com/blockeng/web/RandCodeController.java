package com.blockeng.web;

import com.blockeng.dto.RandCode;
import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.IpUtil;
import com.blockeng.service.RandCodeService;
import com.blockeng.vo.RandCodeForm;
import com.blockeng.vo.mappers.RandCodeMapper;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/api/v1/dm")
@Slf4j
@Api(value = "发送验证码", description = "发送验证码 REST API")
public class RandCodeController {

    @Autowired
    private RandCodeService randCodeService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送验证码
     *
     * @param form
     * @return
     */
    @PostMapping("/sendTo")
    @ApiOperation(value = "发送验证码", notes = "发送验证码", httpMethod = "POST")
    Object sendTo(@RequestBody @Valid RandCodeForm form, BindingResult result) {
        RandCode randCode = RandCodeMapper.INSTANCE.map(form);
        String ip = IpUtil.getIpAddr(request);
        log.info("ip---:" + ip);
        Long keyResult = 0L;
        log.info("phone---:" + form.getPhone());
        if (!Strings.isNullOrEmpty(form.getPhone())) {
            String telKey = "RATE_LIMITER:tel:" + randCode.getTemplateCode() + ":" + randCode.getPhone();
            keyResult = stringRedisTemplate.opsForValue().increment(telKey, 1);
            if (keyResult.equals(1L)) {
                stringRedisTemplate.expire(telKey, 1, TimeUnit.MINUTES);
            }
        } else {
            String emailKey = "RATE_LIMITER:email:" + randCode.getTemplateCode() + ":" + randCode.getEmail();
            keyResult = stringRedisTemplate.opsForValue().increment(emailKey, 1);
            if (keyResult.equals(1L)) {
                stringRedisTemplate.expire(emailKey, 1, TimeUnit.MINUTES);
            }
        }
        String ipKey = "RATE_LIMITER:ip:" + randCode.getTemplateCode() + ":" + ip;
        log.info("写redis:" + form.getPhone());
        Long ipResult = stringRedisTemplate.opsForValue().increment(ipKey, 1);
        if (ipResult.equals(1L)) {
            stringRedisTemplate.expire(ipKey, 1, TimeUnit.DAYS);
        }
        log.info("判断keyresult:" + keyResult);
        if (keyResult <= 3 && ipResult <= 100) {
            log.info("发短信 beging:");
            randCodeService.sendTo(randCode);
            log.info("发短信 end:");
        } else {
            // 短信发送次数超过上限
            throw new GlobalDefaultException(41002);
        }
        return Response.ok();
    }

    /**
     * 校验验证码
     *
     * @return
     */
    @PostMapping("/verify")
    boolean verify(@RequestBody RandCodeVerifyDTO randCodeVerifyDTO) {
        return randCodeService.verify(randCodeVerifyDTO);
    }
}