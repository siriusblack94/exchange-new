package com.blockeng.user.feign;

import com.blockeng.framework.security.UserDetails;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.hystrix.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@FeignClient(value = "exchange-user-service", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @RequestMapping(value = "/user/selectById", method = RequestMethod.POST)
    UserDTO selectById(@RequestParam("userId") Long userId);

    @RequestMapping(value = "/user/loadUserByUsername", method = RequestMethod.POST)
    UserDetails loadUserByUsername(@RequestParam("userName") String username);
}