package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.DESUtil;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.AdminAddress;
import com.blockeng.admin.entity.CoinConfig;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.AdminAddressService;
import com.blockeng.admin.service.CoinConfigService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 * 平台归账手续费等账户 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/adminAddress")
@Api(value = "平台归账手续费等账户", description = "平台归账手续费等账户")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;


    @Autowired
    private CoinConfigService coinConfigService;

    @Autowired
    private DESUtil desUtil;

    @Log(value = "查询平台归账手续费等账户列表", type = SysLogTypeEnum.SELECT)
    @GetMapping("/getByCoinId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "coinId", value = "币种ID", required = false, dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "根据币种ID获取账户列表_分页", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<AdminAddress> page,
                             String coinId) {
        EntityWrapper<AdminAddress> ew = new EntityWrapper<>();
        //过滤密码
        ew.setSqlSelect("id, coin_id AS coinId, address, status, coin_type as coinType");
        if (!Strings.isNullOrEmpty(coinId)) {
            ew.eq("coin_id", coinId);
        } else {
            ew.eq("coin_id", "-1");
        }
        ew.orderBy("id", false);
        return ResultMap.getSuccessfulResult(adminAddressService.selectPage(page, ew));
    }

    @Log(value = "查询账户信息", type = SysLogTypeEnum.SELECT)
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "账户信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String id) {
        AdminAddress address = adminAddressService.selectById(id);
        return ResultMap.getSuccessfulResult(new AdminAddress().setAddress(address.getAddress()).setStatus(address.getStatus()));
    }

    @Log(value = "新增账户平台归账手续费等账户", type = SysLogTypeEnum.INSERT)
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增账户", httpMethod = "POST")
    public Object insert(@RequestBody AdminAddress adminAddress) {

        if (null == adminAddress.getCoinId() || adminAddress.getCoinId() <= 0) {
            return ResultMap.getFailureResult("未找到当前币种");
        }

        CoinConfig coin = coinConfigService.selectById(adminAddress.getCoinId());

        if (StringUtils.isEmpty(coin.getCoinType())) {
            return ResultMap.getFailureResult("未找到当前币种类型,请重新选择当前币种类型.");
        }

        adminAddress.setCoinType(coin.getCoinType());

        if (!Strings.isNullOrEmpty(adminAddress.getPwd())) {
            adminAddress.setPwd(desUtil.encrypt(adminAddress.getPwd()));
        }
        if (!Strings.isNullOrEmpty(adminAddress.getKeystore())) {
            adminAddress.setKeystore(desUtil.encrypt(adminAddress.getKeystore()));
        }

        adminAddress.setCoinType(coin.getCoinType());

        if (adminAddressService.insert(adminAddress)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "修改账户平台归账手续费等账户", type = SysLogTypeEnum.UPDATE)
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改账户")
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(@RequestBody AdminAddress adminAddress) {
        AdminAddress oriAddress = adminAddressService.selectById(adminAddress.getId());
        if (null == oriAddress) {
            return ResultMap.getFailureResult("未找到对应记录!");
        }
        if (StringUtils.isEmpty(adminAddress.getCoinType())) {
            return ResultMap.getSuccessfulResult("请选择币种类型");
        }
        if (!Strings.isNullOrEmpty(adminAddress.getPwd())) {
            adminAddress.setPwd(desUtil.encrypt(adminAddress.getPwd()));
        } else {
            adminAddress.setPwd(null);
        }
        if (!Strings.isNullOrEmpty(adminAddress.getKeystore()) && !oriAddress.getKeystore().equals(adminAddress.getKeystore())) {
            String jm = desUtil.encrypt(adminAddress.getKeystore());
            adminAddress.setKeystore(jm);
        }

        if (adminAddressService.updateById(adminAddress)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
