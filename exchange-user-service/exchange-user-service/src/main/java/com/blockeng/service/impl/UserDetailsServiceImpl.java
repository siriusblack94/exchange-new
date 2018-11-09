package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.UserDetailsMapper;
import com.blockeng.entity.User;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.geetest.GeetestLib;
import com.blockeng.framework.security.JwtToken;
import com.blockeng.framework.security.TokenProvider;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.IpUtil;
import com.blockeng.mapper.UserLoginLogMapper;
import com.blockeng.repository.UserLoginLog;
import com.blockeng.service.UserDetailsService;
import com.blockeng.service.UserService;
import com.blockeng.utils.UserAgentUtils;
import com.blockeng.web.vo.UserLoginForm;
import com.blueconic.browscap.Capabilities;
import com.google.common.base.Strings;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author qiang
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private UserLoginLogMapper userLoginLogMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectByUsername(username);
        UserDetails userDetails = UserDetailsMapper.INSTANCE.toUserDetails(user);
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        userDetails.setAuthorities(authorities);
        return userDetails;
    }

    @Override
    public boolean check(UserLoginForm form) {
        Capabilities capabilities = UserAgentUtils.parse(request.getHeader(HttpHeaders.USER_AGENT));
        String ip = IpUtil.getIpAddr(request);

        //二次验证
        boolean ssiv = Optional.ofNullable(form.getGa_code()).isPresent() && !Strings.isNullOrEmpty(form.getGeetest_challenge()) && !Strings.isNullOrEmpty(form.getGeetest_seccode()) && !Strings.isNullOrEmpty(form.getGeetest_validate());
        int gtResult = 0;
        if (ssiv) {
            gtResult = 1;
        } else {
            //自定义参数,可选择添加
            HashMap<String, String> param = new HashMap<>();
            param.put("user_id", form.getUsername()); //网站用户id
            param.put("client_type", capabilities.getDeviceType()); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
            param.put("ip_address", ip); //传输用户请求验证时所携带的IP
            gtResult = geetestLib.enhencedValidateRequest(form.getGeetest_challenge(), form.getGeetest_validate(), form.getGeetest_seccode(), param);
        }
        if (gtResult == 1) {
            // 验证成功
            return true;
        } else {
            // 验证失败
            throw new GlobalDefaultException(40011);
        }
    }

    /**
     * 用户登录
     *
     * @param form
     * @param request
     * @return
     */
    @Override
    public UserDetails login(UserLoginForm form, HttpServletRequest request) {
//        log.info(form.toString());
        User user = userService.selectByUsername(form.getUsername());
//        if (user!=null){log.info("user:-----"+user.toString());}
        if (!Optional.ofNullable(user).isPresent()) {
            log.error("账号不存在");
            throw new GlobalDefaultException(2018);
        }
        /*boolean flag = randCodeServiceClient.verify(SmsTemplate.LOGIN.getCode(), form.getUsername(), form.getValidateCode());
        if (!flag) {
            //验证码错误
            throw new GlobalDefaultException(20007);
        }*/
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(form.getUsername(), form.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getStatus() == BaseStatus.INVALID.getCode()) {
            // 用户账号被禁用
            throw new GlobalDefaultException(2011);
        }
        // 如果开启了GA验证
        if (userDetails.getGaStatus() == 1) {
            if (Optional.ofNullable(form.getGa_code()).isPresent()) {
                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                if (!gAuth.authorize(userDetails.getGaSecret(), form.getGa_code())) {
                    // 谷歌验证码错误，请重新输入
                    throw new GlobalDefaultException(40004);
                }
            } else {
                // 需要进行二次动态口令验证
                throw new GlobalDefaultException(40014);
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        JwtToken accessToken = tokenProvider.createToken(userDetails);
        userDetails.setAccessToken(accessToken.getJwt());
        userDetails.setExpire(accessToken.getExpire());
        System.out.println("login------------------------");
        /* 2018.2.27 登陆信息没有入库*/
        try {
//            UserLoginLog userLoginLog = new UserLoginLog();
//            if (null != userDetails.getId())
//                userLoginLog.setUserId(userDetails.getId());
//            if (null != userDetails.getAgentNote())
//                userLoginLog.setUserAgent(userDetails.getAgentNote());
//            if (null != userDetails.getAuthtime())
//                userLoginLog.setLoginTime(userDetails.getAuthtime());
//
//            userLoginLog.setLoginIp(request.getRemoteHost());
//            // userLoginLog.setLoginAddress(request.getRemoteAddr());
//            userLoginLogMapper.insert(userLoginLog);
        } catch (Exception e) {
            log.warn("", e);
        }
        return userDetails;
    }

    @Override
    public UserDetails oauth(String access_key, String secret) {
        QueryWrapper<User> ew = new QueryWrapper<>();
        ew.eq("access_key_id", access_key);
        ew.eq("access_key_secret", secret);
        User user = userService.selectOne(ew);
        if (Optional.ofNullable(user).isPresent()) {
            UserDetails userDetails = UserDetailsMapper.INSTANCE.toUserDetails(user);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_OPEN"));
            userDetails.setAuthorities(authorities);
            JwtToken accessToken = tokenProvider.createToken(userDetails);
            userDetails.setAccessToken(accessToken.getJwt());
            userDetails.setExpire(accessToken.getExpire());
            return userDetails;
        } else {
            //无效的access_key_id
            throw new GlobalDefaultException(40012);
        }
    }

    @Override
    public JwtToken refreshToken(String accessToken) {
        return tokenProvider.refreshToken(accessToken);
    }
}