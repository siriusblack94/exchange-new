package com.blockeng.service;

import com.blockeng.framework.security.JwtToken;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.web.vo.UserLoginForm;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiang
 */
public interface UserDetailsService extends org.springframework.security.core.userdetails.UserDetailsService {

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 校验环境
     *
     * @param form
     * @return
     */
    boolean check(UserLoginForm form);

    /**
     * 登录
     *
     * @param form
     * @param request
     * @return
     */
    UserDetails login(UserLoginForm form, HttpServletRequest request);

    UserDetails oauth(String access_key, String secret);

    JwtToken refreshToken(String oldToken);
}