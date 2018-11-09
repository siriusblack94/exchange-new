package com.blockeng.admin.aop;


import com.blockeng.admin.entity.Config;
import com.blockeng.admin.exception.RequestLimitException;
import com.blockeng.admin.service.ConfigService;
import com.blockeng.framework.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


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


    @Before("@annotation(com.blockeng.admin.annotation.RequestIpLimit)")
    public void requestIpLimit(final JoinPoint joinPoint) throws  Exception {
            String ip = IpUtil.getIpAddr(request);
             log.info("当前访问者IP："+ ip);
             Config ipConfig = configService.queryBuyCodeAndType("SYSTEM", "REQUEST_IP_LIMIT");
            if (ip==null||ipConfig==null|| ipConfig.getValue()==null)  throw new RequestLimitException("ipConfig未配置或者无法获取访问者IP");
             String[] ips = ipConfig.getValue().split(",");
            Set<String> ipsSet = new HashSet<>(Arrays.asList(ips));
            log.info("系统配置IP："+ ipsSet);
            if (!ipsSet.contains(ip)) {
                throw new RequestLimitException("非法访问");
            }
    }



}