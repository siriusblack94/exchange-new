package com.blockeng.user.feign.hystrix;

import com.blockeng.user.dto.UserBankDTO;
import com.blockeng.user.feign.UserBankServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
public class UserBankServiceClientFallback implements UserBankServiceClient {

    @Override
    public UserBankDTO selectByUserId(Long userId) {
        return null;
    }
}
