package com.blockeng.user.feign.hystrix;

import com.blockeng.framework.security.UserDetails;
import com.blockeng.user.dto.UserDTO;
import com.blockeng.user.feign.UserServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserDTO selectById(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return null;
    }
}
