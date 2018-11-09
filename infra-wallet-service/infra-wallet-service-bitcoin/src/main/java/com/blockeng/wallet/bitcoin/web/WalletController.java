package com.blockeng.wallet.bitcoin.web;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.CoinConfigService;
import com.blockeng.wallet.service.UserAddressService;
import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.newclient.Client;
import com.clg.wallet.newclient.ClientFactory;
import com.clg.wallet.wallet.Omni.OmniNewClient;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: sirius
 * @Date: 2018/9/27 16:34
 * @Description:
 */
@RestController
@RequestMapping("/info")
@Slf4j
@Api(value = "钱包查询", description = "钱包查询")
public class WalletController {
    @Autowired
    private ClientInfo clientInfo;
    @Autowired
    private  CoinConfigService  coinConfigService;


    @GetMapping(value = "/balance/{id}")
    public Object getBalance(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id))  return "error";
        EntityWrapper<CoinConfig> ew = new EntityWrapper<>();
        ew.eq("status", 1);
        ew.eq("id", id);
        CoinConfig coinConfig = coinConfigService.selectOne(ew);
        if (coinConfig==null)  return "error";
        ClientBean coin = clientInfo.getClientInfoFromId(Long.valueOf(id));
        OmniNewClient client = new OmniNewClient(coin);
        Map<String,Object> map = new HashMap<>();
        BigDecimal balanceIn = client.getBalance().toBigDecimal();
        BigDecimal balanceOut = client.getBalanceOut().toBigDecimal();

        log.info("--balanceIn---"+balanceIn);
        log.info("--balanceOut---"+balanceOut);
        map.put("balanceIn",balanceIn);
        map.put("balanceOut",balanceOut);
        map.put("name",coinConfig.getName());
        return JSON.toJSONString(map);

    }

}
