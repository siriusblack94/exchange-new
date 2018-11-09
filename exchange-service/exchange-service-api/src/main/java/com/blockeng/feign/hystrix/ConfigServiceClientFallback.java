package com.blockeng.feign.hystrix;

import com.blockeng.dto.ConfigDTO;
import com.blockeng.feign.ConfigServiceClient;
import org.springframework.stereotype.Component;

/**
 * 配置信息
 * by crow
 * 2018年5月19日14:38:00
 */
@Component
public class ConfigServiceClientFallback implements ConfigServiceClient {


    @Override
    public ConfigDTO getConfig(String type, String code) {
        return null;
    }
}
