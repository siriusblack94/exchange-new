package com.blockeng.feign.hystrix;

import com.blockeng.dto.CoinTransferForm;
import com.blockeng.feign.CoinTransferServiceClient;
import org.springframework.stereotype.Component;

/**
 * @Author: jakiro
 * @Date: 2018-11-02 10:26
 * @Description:
 */
@Component
public class CoinTransferServiceClientFallback implements CoinTransferServiceClient {

    @Override
    public boolean doTransfer(CoinTransferForm coinTransferForm) {
        return false;
    }
}
