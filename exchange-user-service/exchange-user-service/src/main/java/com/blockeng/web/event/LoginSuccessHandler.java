package com.blockeng.web.event;

import com.blockeng.api.IpLocApi;
import com.blockeng.dto.UserDetailsMapper;
import com.blockeng.entity.IpInfo;
import com.blockeng.entity.UserLoginLog;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.framework.utils.IpUtil;
import com.blockeng.service.UserLoginLogService;
import com.blockeng.utils.UserAgentUtils;
import com.blueconic.browscap.Capabilities;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

/**
 * @author qiang
 */
@Component
@Log4j2
public class LoginSuccessHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserLoginLogService userLoginLogService;

    @EventListener({AuthenticationSuccessEvent.class, InteractiveAuthenticationSuccessEvent.class})
    public void processAuthenticationSuccessEvent(AbstractAuthenticationEvent event) {
        //用户名密码认证
        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
            UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
            UserLoginLog userLoginLog = UserDetailsMapper.INSTANCE.toUserLoginLog(userDetails);
            String ip = IpUtil.getIpAddr(request);
            userLoginLog.setLoginIp(ip);
            IpInfo ipInfo = IpLocApi.getIpInfo(ip);
            if (Optional.ofNullable(ipInfo).isPresent()) {
                userLoginLog.setLoginAddress(ipInfo.getArea() + ipInfo.getRegion() + ipInfo.getCountry() + ipInfo.getCountry());
            }

            request.getHeader(HttpHeaders.USER_AGENT);
            Capabilities capabilities = UserAgentUtils.parse(request.getHeader(HttpHeaders.USER_AGENT));
            switch (capabilities.getDeviceType()) {
                case "web":
                    userLoginLog.setClientType(1); // 0是未知，1是pc，2是安卓，3是iphone,4是h5
                case "h5":
                    userLoginLog.setClientType(4);
                default:
                    userLoginLog.setClientType(0);
            }
            userLoginLog.setLoginTime(new Date());
            userLoginLog.setId(null);
            userLoginLogService.insert(userLoginLog);


        }
    }
}
