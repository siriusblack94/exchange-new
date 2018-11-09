package com.blockeng.feign;


import com.blockeng.dto.CoinDTO;
import com.blockeng.feign.hystrix.CoinServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 币种信息获取
 * by crow
 * 2018年5月19日14:38:00
 */
@FeignClient(value = "exchange-service", fallback = CoinServiceClientFallback.class)
public interface CoinServiceClient {

    /**
     * 获取数字货币coin 初始化数字货币账户
     */
    @RequestMapping("/coin/trade/wallet")
    List<CoinDTO> selectCoin();

    @RequestMapping("/coin/allCoin")
    List<CoinDTO> allCoin();
}
