package com.blockeng.feign.hystrix;

import com.blockeng.dto.CoinDTO;
import com.blockeng.feign.CoinServiceClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CoinServiceClientFallback implements CoinServiceClient {

    @Override
    public List<CoinDTO> selectCoin() {
        return new ArrayList<>();
    }

    @Override
    public List<CoinDTO> allCoin() {
        return new ArrayList<>();
    }
}
