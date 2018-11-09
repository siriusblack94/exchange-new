package com.blockeng.admin.web.wallet;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.PrivatePlacementDTO;

import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.WalletInfo;
import com.blockeng.admin.entity.Config;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: sirius
 * @Date: 2018/9/26 17:33
 * @Description:
 */
@RestController
@RequestMapping("/wallet")
@Slf4j
@Api(value = "钱包查询", description = "钱包查询")
public class WalletInfoController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ConfigService configService;

    @Autowired
    private CoinRechargeService coinRechargeService;

    @Autowired
    private CoinWithdrawService coinWithdrawService;

    @Autowired
    private WalletInfoService walletInfoService;

    @Autowired
    private CoinService coinService;



    @Log(value = "钱包查询", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('wallet_query')")
    @GetMapping("/info")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "钱包查询", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "币种ID", dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object getInfo(@RequestParam(required = true) String id) {
        EntityWrapper<WalletInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("coin_id", id);
        WalletInfo walletInfo = walletInfoService.selectOne(wrapper);
        if (walletInfo==null) return ResultMap.getFailureResult("当前没有币种信息，请更新") ;
        return   ResultMap.getSuccessfulResult(walletInfo);
    }



    @Log(value = "eth钱包更新", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('eth_wallet_update')")
    @GetMapping("/eth/update")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "eth钱包更新", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "币种ID", dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object updateEthInfo(@RequestParam(required = true) String id) {
        Config config = configService.queryBuyCodeAndType("SYSTEM", "WALLET_ETH_IP_PORT");
        if (config==null||StringUtils.isBlank(config.getValue())) return ResultMap.getFailureResult("未配置WALLET_ETH_IP_PORT");
        String walletUrl=config.getValue();
        Date  start = new Date();
        Map<String,Object> map = getWalletInfo(id,walletUrl);
        if (map==null) return ResultMap.getFailureResult("不存在有效用户币种地址");
        BigDecimal walletBalanceTotal = (BigDecimal) map.get("balanceTotal");
        Integer walletUserCount = (Integer) map.get("count");
        String name = (String) map.get("name");
        Date  end = new Date();
        WalletInfo walletInfo = getResult(id);
        walletInfo.setWalletBalanceTotal(walletBalanceTotal)
                .setWalletUserCount(walletUserCount)
                .setName(name)
                .setDate(start)
                .setCreated(end);
       if (walletInfoService.insertOrUpdate(walletInfo)) return ResultMap.getSuccessfulResult(walletInfo);
        return  ResultMap.getFailureResult("更新异常");
    }




    @Log(value = "btc钱包更新", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('btc_wallet_update')")
    @GetMapping("/btc/update")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "btc钱包更新", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "eth钱包类型", dataType = "String"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = PrivatePlacementDTO.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public Object updateBtcInfo(@RequestParam(required = true) String id) {
        Config config = configService.queryBuyCodeAndType("SYSTEM", "WALLET_BTC_IP_PORT");
        if (config==null||StringUtils.isBlank(config.getValue())) return ResultMap.getFailureResult("未配置WALLET_BTC_IP_PORT");
        String   walletBtcUrl=config.getValue();
        Date  start = new Date();
        Map<String,Object> map= getWalletInfo(id, walletBtcUrl);
        if (map==null) return ResultMap.getFailureResult("不存在有效用户币种地址");
        BigDecimal balanceIn = (BigDecimal) map.get("balanceIn");
        BigDecimal balanceOut = (BigDecimal) map.get("balanceOut");
        String name = (String) map.get("name");
        Date  end = new Date();
        WalletInfo walletInfo = getResult(id);
        walletInfo.setBalanceTotalIn(balanceIn)
                .setBalanceTotalOut(balanceOut)
                .setName(name)
                .setDate(start)
                .setCreated(end);
        if (walletInfoService.insertOrUpdate(walletInfo))  return ResultMap.getSuccessfulResult(walletInfo);
        return  ResultMap.getFailureResult("更新异常");
    }

    @GetMapping("/eth/list")
    public Object getEthList() {
        EntityWrapper<Coin> wrapper = new EntityWrapper<>();
        wrapper.eq("status",1).andNew().eq("type", "ethToken").or().eq("type","eth");
        List<Coin>  coins= coinService.selectList(wrapper);
        if(coins==null||coins.size()==0)  ResultMap.getFailureResult("获取币种类型异常");
        return   ResultMap.getSuccessfulResult(coins);
    }
    @GetMapping("/btc/list")
    public Object getBtcList() {
        EntityWrapper<Coin> wrapper = new EntityWrapper<>();
        wrapper.eq("type", "btc");
        wrapper.eq("status",1);
        List<Coin>  coins= coinService.selectList(wrapper);
        if(coins==null||coins.size()==0)  ResultMap.getFailureResult("获取币种类型异常");
        return   ResultMap.getSuccessfulResult(coins);
    }


    private WalletInfo getResult( String id) {
        BigDecimal coinRecharge= coinRechargeService.selectAmountByCoinId(id);
        if (coinRecharge==null) coinRecharge=BigDecimal.ZERO;
        BigDecimal coinWithdraw = coinWithdrawService.selectAmountByCoinId(id);
        if (coinWithdraw==null) coinWithdraw=BigDecimal.ZERO;
        Integer  userCount = accountService.selectBalanceCountByCoinId(id);
        BigDecimal  balanceTotal = accountService.selectAmount(id);
        EntityWrapper<WalletInfo> wrapper = new EntityWrapper<>();
        wrapper.eq("coin_id", id);
        WalletInfo walletInfo = walletInfoService.selectOne(wrapper);
        if (walletInfo==null) walletInfo=new WalletInfo();
        walletInfo
                .setBalanceTotal(balanceTotal)
                .setUserCount(userCount)
                .setCoinId(Long.valueOf(id))
                .setCoinRechargeAmount(coinRecharge)
                .setCoinWithdrawAmount(coinWithdraw);
        return walletInfo;
    }
    private  Map<String,Object> getWalletInfo(String id, String walletUrl) {
        if(id==null||id.trim().equals("")) return null;
        String returnXml ="" ;
        try {
            log.info("--wallet.url----"+walletUrl);
            returnXml = restTemplate.getForObject("http://"+walletUrl+"/info/balance/{1}", String.class, id);
            // 转码原因：RestTemplate默认是使用org.springframework.http.converter.StringHttpMessageConverter来解析
            // StringHttpMessageConverter 默认用的 ISO-8859-1来编码的
            returnXml = new String(returnXml.getBytes("ISO-8859-1"), "utf-8");
            log.info("返回---balance--"+returnXml);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        if("error".equals(returnXml)||"".equals(returnXml)) return null;
        return JSON.parseObject(returnXml);
    }
}
