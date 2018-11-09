package com.blockeng.framework.security.jwt;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.framework.security.TokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qiang
 */
@Component("authenticationFilter")
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;

    @Autowired
    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        try {
            String token = tokenProvider.resolveToken(httpServletRequest);

            if (StringUtils.hasText(token)) {
                if (this.tokenProvider.validateToken(token)) {
                    Authentication authentication = this.tokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(servletRequest, httpServletResponse);
        } catch (ExpiredJwtException eje) {
            servletResponse.setContentType("application/json;charset=UTF-8");
            log.info("Security exception for user {} - {}", eje.getClaims().getSubject(), eje.getMessage());
            JSONObject result = new JSONObject();
            result.put("errcode", 40001);
            result.put("errmsg", eje.getMessage());
            httpServletResponse.getWriter().write(result.toJSONString());
        } catch (MalformedJwtException e) {
            servletResponse.setContentType("application/json;charset=UTF-8");
            log.info(e.getMessage());
            JSONObject result = new JSONObject();
            result.put("errcode", 41001);
            result.put("errmsg", "access_token missing hint");
            httpServletResponse.getWriter().write(result.toJSONString());
        }
    }
}