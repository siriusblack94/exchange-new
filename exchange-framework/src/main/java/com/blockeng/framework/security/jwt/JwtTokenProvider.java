package com.blockeng.framework.security.jwt;

import com.blockeng.framework.config.JwtProperties;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.security.JwtToken;
import com.blockeng.framework.security.TokenProvider;
import com.blockeng.framework.security.UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author qiang
 */
@Component
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenProvider implements TokenProvider {

    private static final String ID = "id";
    private static final String TEL = "tel";
    private static final String EMAIL = "email";
    private static final String COUNTRY_CODE = "country_code";
    private static final String AUTHORITIES = "auth";

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public JwtToken createToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date exp = new Date(now + jwtProperties.getExpire());
        String jwt = Constant.BEARER + Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim(AUTHORITIES, authorities)
                .claim(ID, userDetails.getId())
                .claim(TEL, userDetails.getMobile())
                .claim(EMAIL, userDetails.getEmail())
                .claim(COUNTRY_CODE, userDetails.getCountryCode())
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .setExpiration(exp)
                .compact();
        return new JwtToken(jwt, jwtProperties.getExpire());
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(accessToken)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(claims.get(AUTHORITIES).toString().split(",")).stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());

        Long id = Long.valueOf(claims.get(ID).toString());
        String countryCode = claims.get("country_code", String.class);
        String tel = claims.get("tel", String.class);
        String email = claims.get("email", String.class);

        UserDetails principal = new UserDetails();
        principal.setId(id);
        principal.setUsername(claims.getSubject());
        principal.setCountryCode(countryCode);
        principal.setMobile(tel);
        principal.setEmail(email);
        principal.setAuthorities(authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    @Override
    public boolean validateToken(String accessToken) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: " + e.getMessage());
            return false;
        }
    }

    @Override
    public JwtToken refreshToken(String accessToken) {
        Authentication authentication = getAuthentication(accessToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtToken refreshedToken = createToken(userDetails);
        return refreshedToken;
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constant.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constant.BEARER)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}