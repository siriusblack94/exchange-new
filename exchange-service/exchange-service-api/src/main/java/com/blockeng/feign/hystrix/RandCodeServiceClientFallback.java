package com.blockeng.feign.hystrix;

import com.blockeng.dto.RandCodeVerifyDTO;
import com.blockeng.feign.RandCodeServiceClient;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
public class RandCodeServiceClientFallback implements RandCodeServiceClient {

    @Override
    public boolean verify(RandCodeVerifyDTO randCodeVerifyDTO) {
        return false;
    }
}
