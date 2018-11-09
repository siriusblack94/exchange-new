package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.CoinDTO;
import com.blockeng.dto.CoinDTOMapper;
import com.blockeng.entity.Coin;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.WalletType;
import com.blockeng.framework.http.Response;
import com.blockeng.service.CoinService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 币币交易币种Controller
 */
@RestController
@RequestMapping("/coin")
@Slf4j
public class CoinController {

    @Autowired
    private CoinService coinService;

    /**
     * 查询有效的钱包币
     *
     * @return
     */
    @GetMapping("/trade/wallet")
    @ApiOperation(value = "COIN-001 获取钱包币信息", notes = "获取钱包币信息", httpMethod = "GET"
            , authorizations = {@Authorization(value = "Authorization")})
    public Object walletCoin() {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.EFFECTIVE.getCode())
                .eq("wallet", WalletType.QBB.getCode());
        return Response.ok(coinService.selectList(wrapper));
    }

    /**
     * 查找数字货币基础币
     */
    @GetMapping("/baseCoin")
    @ApiOperation(value = "获取数字货币基础币列表", notes = "获取数字货币基础币列表", httpMethod = "GET")
    public Object baseCoin() {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        return Response.ok(coinService.selectList(wrapper));
    }


    @GetMapping("/allCoin")
    @ApiOperation(value = "获取数字货币基础币列表", notes = "获取数字货币基础币列表", httpMethod = "GET")
    public List<CoinDTO> allCoin() {
        QueryWrapper<Coin> wrapper = new QueryWrapper<>();
        return CoinDTOMapper.INSTANCE.from(coinService.selectList(wrapper));
    }
}
