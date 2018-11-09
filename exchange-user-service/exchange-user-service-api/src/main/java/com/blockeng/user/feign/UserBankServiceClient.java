package com.blockeng.user.feign;

import com.blockeng.user.dto.UserBankDTO;
import com.blockeng.user.feign.hystrix.UserBankServiceClientFallback;
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
@FeignClient(value = "exchange-user-service", fallback = UserBankServiceClientFallback.class)
public interface UserBankServiceClient {

    @RequestMapping(value = "/v3/cards/selectUserBankByUserId", method = RequestMethod.POST)
    UserBankDTO selectByUserId(@RequestParam("userId") Long userId);
}