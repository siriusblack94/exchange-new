package com.blockeng.framework.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author qiang
 */
public interface TokenProvider {

    JwtToken createToken(UserDetails userDetails);

    boolean validateToken(String accessToken);

    JwtToken refreshToken(String accessToken);

    String resolveToken(HttpServletRequest request);

    Authentication getAuthentication(String accessToken);
}