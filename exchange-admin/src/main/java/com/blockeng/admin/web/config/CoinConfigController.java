package com.blockeng.admin.web.config;

import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.DESUtil;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.CoinConfig;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinConfigService;
import com.blockeng.admin.service.CoinService;
import com.google.common.base.Strings;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 币种配置信息 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-17
 */
@RestController
@RequestMapping("/coinConfig")
@Api(value = "币种配置", description = "币种配置管理")
public class CoinConfigController {

    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private DESUtil desUtil;

    @Log(value = "查询币种配置信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_query')")
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种配置信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String id) {
        CoinConfig coinConfig = coinConfigService.selectById(id);
        if (null == coinConfig) {
            return ResultMap.getFailureResult("未找到对应币种配置!");
        }
        coinConfig.setRpcPwd(null);//过滤密码显示
        coinConfig.setRpcPwdOut(null);//过滤密码显示
        coinConfig.setWalletPass(null);//过滤密码显示
        coinConfig.setWalletPassOut(null);//过滤密码显示
        return ResultMap.getSuccessfulResult(coinConfig);
    }

    @Log(value = "新增币种配置", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('trade_coin_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增币种配置", httpMethod = "POST")
    public Object insert(@RequestBody CoinConfig coinConfig) {
        if (null == coinConfig.getId() || 0 >= coinConfig.getId()) {
            return ResultMap.getFailureResult("请输入正确的币种ID!");
        }
        Coin coin = coinService.selectById(coinConfig.getId());
        if (null == coin) {
            return ResultMap.getFailureResult("未找到对应币种,请检查ID!");
        }
        coinConfig.setCoinType(coin.getType());
        if (!Strings.isNullOrEmpty(coinConfig.getRpcPwd())) {
            coinConfig.setRpcPwd(desUtil.encrypt(coinConfig.getRpcPwd()));
        }
        if (!Strings.isNullOrEmpty(coinConfig.getRpcPwdOut())) {
            coinConfig.setRpcPwdOut(desUtil.encrypt(coinConfig.getRpcPwdOut()));
        }
        if(!Strings.isNullOrEmpty(coinConfig.getWalletPass())){
            coinConfig.setWalletPass(desUtil.encrypt(coinConfig.getWalletPass()));
        }
        if(!Strings.isNullOrEmpty(coinConfig.getWalletPassOut())){
            coinConfig.setWalletPassOut(desUtil.encrypt(coinConfig.getWalletPassOut()));
        }
        //Name也与coin保持一致
        coinConfig.setName(coin.getName());
        if (coinConfigService.insert(coinConfig)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "修改or新增币种配置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改or新增币种配置")
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(@RequestBody CoinConfig coinConfig) {
        Coin coin = coinService.selectById(coinConfig.getId());
        if (null == coin) {
            return ResultMap.getFailureResult("未找到对应币种,请检查ID!");
        }
        //Name也与coin保持一致
        coinConfig.setName(coin.getName());
        coinConfig.setCoinType(coin.getType());
        coinConfig.setStatus(coinConfig.getStatus());
        if (!Strings.isNullOrEmpty(coinConfig.getRpcPwd())) {
            coinConfig.setRpcPwd(desUtil.encrypt(coinConfig.getRpcPwd()));
        }
        if (!Strings.isNullOrEmpty(coinConfig.getRpcPwdOut())) {
            coinConfig.setRpcPwdOut(desUtil.encrypt(coinConfig.getRpcPwdOut()));
        }
        if(!Strings.isNullOrEmpty(coinConfig.getWalletPass())){
            coinConfig.setWalletPass(desUtil.encrypt(coinConfig.getWalletPass()));
        }
        if(!Strings.isNullOrEmpty(coinConfig.getWalletPassOut())){
            coinConfig.setWalletPassOut(desUtil.encrypt(coinConfig.getWalletPassOut()));
        }
        if (coinConfigService.insertOrUpdate(coinConfig)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");

        }
    }
}
