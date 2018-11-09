package com.blockeng.wallet.web;


import com.blockeng.wallet.dto.WalletResultCode;
import com.blockeng.wallet.dto.WalletResultDTO;
import com.blockeng.wallet.entity.CoinConfig;
import com.blockeng.wallet.exception.CoinException;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.service.CoinConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 币种配置信息 前端控制器
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@RestController
@RequestMapping("/coinConfig")
public class CoinConfigController {
    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private ClientInfo clientInfo;

    /**
     * 更改钱包密码
     */

    @PostMapping("/changePass")
    @ResponseBody
    @ApiOperation(value = "order-001 更改钱包密码", notes = "更改钱包密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "更改钱包密码,新密码", value = "newPass", required = true, dataType = "String"),
            @ApiImplicitParam(name = "更改钱包密码,老密码", value = "oldPass", required = true, dataType = "String"),
            @ApiImplicitParam(name = "币种Id", value = "coinId", required = true, dataType = "Long")

    })
    WalletResultDTO changePass(@ApiIgnore String newPass, @ApiIgnore String oldPass, @ApiIgnore Long coinId) {
        if (StringUtils.isEmpty(newPass) || StringUtils.isEmpty(oldPass) || null == coinId || coinId <= 0) {//校验参数
            return new WalletResultDTO().setStatusCode(WalletResultCode.USER_NOT_LONG.getCode());
        }
        CoinConfig coinConfig = clientInfo.getCoinConfigFormId(coinId);
        if (null == coinConfig) {
            return new WalletResultDTO().setStatusCode(WalletResultCode.NOT_EXIST_COIN_ID.getCode());
        }
        try {
            return coinConfigService.updateCoinPass(newPass, oldPass, coinConfig);
        } catch (CoinException e) {
            return new WalletResultDTO().setStatusCode(WalletResultCode.SUCCESS.getCode()).setResultDesc(e.getMessage());
        }
    }

}

