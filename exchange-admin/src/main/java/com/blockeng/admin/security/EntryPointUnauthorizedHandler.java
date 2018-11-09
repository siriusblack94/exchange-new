package com.blockeng.admin.security;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定401返回值
 *
 * @author qiang
 */
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("application/json");
        response.setStatus(200);
        JSONObject result = new JSONObject();
        String authorization = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(authorization)) {
            result.put("errcode", 40001);
            result.put("errmsg", "Bad credentials");
        } else {
            result.put("errcode", 41001);
            result.put("errmsg", "access_token missing hint.");
        }
        response.getWriter().write(result.toJSONString());
    }
}