package com.blockeng.extend.handler;

import com.blockeng.extend.exception.RequestLimitException;
import com.blockeng.extend.service.ConfigService;
import com.blockeng.extend.util.IPUtil;
import com.blockeng.framework.geetest.GeetestLib;
import com.blockeng.framework.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @Auther: sirius
 * @Date: 2018/10/30 14:49
 * @Description:
 */
@Aspect
@Component
@Slf4j
public class RequestLimitContract {
    @Autowired
    private ConfigService configService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private GeetestLib geetestLib;

    @Before("@annotation(com.blockeng.extend.annotation.RequestIpLimit)")
    public void requestIpLimit(final JoinPoint joinPoint) throws  Exception {
            String ip =IPUtil.getIpAddress(request);
             log.info("当前访问者IP："+ ip);
            String ipConfig = configService.queryByTypeAndCode("SYSTEM", "EXTEND_SYNCHRONOUS_IP").getValue();
            if (ip==null||ipConfig==null)  throw new RequestLimitException("ipConfig未配置或者无法获取访问者IP");
             String[] ips = ipConfig.split(",");
            Set<String> ipsSet = new HashSet<>(Arrays.asList(ips));
            log.info("系统配置IP："+ ipsSet);
            if (!ipsSet.contains(ip)) {
                throw new RequestLimitException("非法访问");
            }
    }

    @Before("@annotation(com.blockeng.extend.annotation.RequestGeetestLimit)")
    public void requestGeetestLimit(final JoinPoint joinPoint) throws  Exception {
        String ip =IPUtil.getIpAddress(request);
        log.info("当前访问者IP："+ ip);
        if (ip==null)  throw new RequestLimitException("ipConfig未配置或者无法获取访问者IP");
        // 自定义参数,可选择添加
        String geetest_challenge = request.getParameter("geetest_challenge");
        String geetest_validate = request.getParameter("geetest_validate");
        String geetest_seccode = request.getParameter("geetest_seccode");
        HashMap<String, String> param = new HashMap<>();
        param.put("user_id", "qiang");   // 网站用户id
        param.put("client_type", "web"); // web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip);     // 传输用户请求验证时所携带的IP
        int gtResult = geetestLib.enhencedValidateRequest(geetest_challenge, geetest_validate, geetest_seccode, param);
        if (gtResult != 1) {
            throw new RequestLimitException("行为验证未通过");
        }


    }

}