package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.CoinDTO;
import com.blockeng.admin.entity.*;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AccountService;
import com.blockeng.admin.service.CoinConfigService;
import com.blockeng.admin.service.CoinService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 币种配置信息 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Slf4j
@RestController
@RequestMapping("/coin")
@Api(value = "币种", description = "币种管理")
public class CoinController {


    @Autowired
    private CoinService coinService;

    @Log(value = "查询币种列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "ID", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "币种名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型:xnb 人民币,default 比特币系列,ETH 以太坊,ethToken 以太坊代币", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "wallet_type", value = "钱包类型:rgb 认购币 nqbb 钱包币", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态:0 禁用 1启用", dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询币种列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<Coin> page, String id
            , String name, String title,
                             String type, String wallet_type,
                             String status) {
        EntityWrapper<Coin> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(id)) {
            ew.eq("id", id);
        }
        if (!Strings.isNullOrEmpty(name)) {
            ew.like("name", name);
        }
        if (!Strings.isNullOrEmpty(title)) {
            ew.like("title", title);
        }
        if (!Strings.isNullOrEmpty(type)) {
            ew.eq("type", type);
        }
        if (!Strings.isNullOrEmpty(wallet_type)) {
            ew.eq("wallet", wallet_type);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("status", status);
        }
        ew.orderBy("created", false);
        return ResultMap.getSuccessfulResult(coinService.selectPage(page, ew));
    }

    @Log(value = "所有币种列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_query')")
    @GetMapping("/all")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "所有币种列表", httpMethod = "GET")
    @ApiImplicitParam(name = "status", value = "状态:0 禁用 1启用", dataType = "String", paramType = "query")
    public Object selectAll(String status) {
        EntityWrapper<Coin> ew = new EntityWrapper<Coin>();
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        return ResultMap.getSuccessfulResult(coinService.selectList(ew));
    }

    @Log(value = "币种信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_query')")
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String id) {
        return ResultMap.getSuccessfulResult(coinService.selectById(id));
    }

    @Log(value = "所有币种信息(qbb币种)", type = SysLogTypeEnum.SELECT)
    @GetMapping("/allQbb")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "所有币种信息(qbb币种)", httpMethod = "GET")
    public Object selectAllQbb() {
        EntityWrapper<Coin> ew = new EntityWrapper<>();
        ew.eq("wallet", "qbb");
        ew.notLike("type", "Token");
        return ResultMap.getSuccessfulResult(coinService.selectList(ew));
    }

    @Log(value = "新增币种", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('trade_coin_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增币种", httpMethod = "POST")
    @Transactional(rollbackFor = Exception.class)
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = CoinDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object insert(@RequestBody Coin coin) {
        return coinService.insertCoin(coin);
    }

    @Log(value = "修改币种", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改币种")
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(@RequestBody Coin coin) {
        return
                coinService.updateCoin(coin);
    }

    @Log(value = "币种状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种状态设置(status:1启用 0禁用)")
    @RequestMapping(path = "/setStatus", method = RequestMethod.POST)
    public Object setStatus(@RequestBody Coin coin) {
        Coin oriCoin = coinService.selectById(coin.getId());
        if (null == oriCoin) {
            return ResultMap.getFailureResult("该记录不存在!");
        }
        if (null == coin.getStatus() ||
                coin.getStatus() > 1 ||
                coin.getStatus() < 0) {
            return ResultMap.getFailureResult("输入参数有误!");
        }
        if (coin.getStatus().equals(oriCoin.getStatus())) {
            return ResultMap.getFailureResult("当前状态不能执行此操作!");
        }
        Coin newCoin = new Coin();
        newCoin.setId(coin.getId());
        newCoin.setStatus(coin.getStatus());
        CoinConfig coinConfig = new CoinConfig();
        coinConfig.setId(coin.getId());
        coinConfig.setStatus(coin.getStatus());
        if (coinService.updateConfig(newCoin,coinConfig)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "币种状态设置_批量", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种状态设置_批量")
    @RequestMapping(path = "/batchSetStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "ids:1,2,3", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "1启用 0禁用", required = true, dataType = "int"),
    })
    public Object batchSetStatus(@RequestBody String ids, Integer status) {
        if (Strings.isNullOrEmpty(ids)) {
            return ResultMap.getFailureResult("ID不能为空!");
        }
        if (status > 1
                || status < 0
                || status == null) {
            return ResultMap.getFailureResult("状态参数有误!");
        }
        List<Coin> coinList = coinService.selectList(new EntityWrapper<Coin>().in("id", ids));
        List<Coin> newCoinList = new ArrayList<>();
        if (coinList.size() < 1) {
            return ResultMap.getFailureResult("记录为空!");
        }
        Coin newCoin;
        for (Coin coin : coinList) {
            if (coin.getStatus().equals(status)) {
                return ResultMap.getFailureResult("id:[" + coin.getId() + "]当前状态不能执行此操作,请刷新重试!");
            }
            newCoin = new Coin();
            newCoin.setId(coin.getId());
            newCoin.setStatus(status);
            newCoinList.add(newCoin);
        }
        if (coinService.updateBatchById(newCoinList)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
//ss4DJ4tjJcWmT4SALCRs6bKdVHB1y   rJAhpN3SMztnaA8vddZC1RPJkcCt55uDgS
//ssHpDCSyW4K4zX5tdmapuERarAX6B   rsPMXVweoCHPNZNjYpzwzWpfmmy6mQp7x1