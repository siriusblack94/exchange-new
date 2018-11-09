package com.blockeng.framework.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiang
 */
@Component("authenticationEntryPoint")
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {
        JSONObject result = new JSONObject();
        String authorization = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(authorization)) {
            result.put("errcode", 40001);
            result.put("errmsg", "invalid credential, access_token is invalid or not latest hint.");
        } else {
            result.put("errcode", 41001);
            result.put("errmsg", "access_token missing hint.");
        }
        response.getWriter().write(result.toJSONString());
    }
}