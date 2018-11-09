package com.blockeng.admin.web.ucenter;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.UserAddress;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.service.UserAddressService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 用户钱包地址 前端控制器
 * </p>
 *
 * @author lxl
 * @since 2018-05-19
 */
@RestController
@RequestMapping("/userAddress")
@Api(value = "用户钱包地址controller", tags = {"用户钱包地址"})
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @Autowired
    private CoinService coinService;

    @Log(value = "查询用户的钱包地址", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('user_wallet_address_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String", paramType = "query")
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "用户的钱包地址", httpMethod = "GET")
    @RequestMapping(method = RequestMethod.GET, value = "selectPage")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功", response = UserAddress.class),
            @ApiResponse(code = 1, message = "失败")
    })
    public ResultMap selectUserWalletList(int size, int current, Long userId) {
        Page<UserAddress> page = new Page<UserAddress>(current, size);
        EntityWrapper<UserAddress> ew = new EntityWrapper<>();
        if (userId == null) {
            return ResultMap.getFailureResult("用户id不能为空！");
        }
        ew.eq("user_id", userId);
        ew.orderBy("id", false);
        List<UserAddress> userAddressList = userAddressService.selectPage(page, ew).getRecords();
        if (userAddressList != null && userAddressList.size() > 0) {
            Long coinIds[] = new Long[userAddressList.size()];
            int i = 0;
            for (UserAddress u : userAddressList) {
                coinIds[i] = u.getCoinId();
                i++;
            }
            EntityWrapper<Coin> ew1 = new EntityWrapper<>();
            ew1.in("id", coinIds);
            List<Coin> coinLis = coinService.selectList(ew1);
            for (UserAddress u : userAddressList) {
                for (Coin cn : coinLis) {
                    if (u.getCoinId().equals(cn.getId())) {
                        u.setCoinName(cn.getName());
                    }
                }
            }
        }
        page.setRecords(userAddressList);
        return ResultMap.getSuccessfulResult(page);
    }
}
