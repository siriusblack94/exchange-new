package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.entity.Coin;
import com.blockeng.admin.entity.CoinConfig;
import com.blockeng.admin.entity.CoinType;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.CoinConfigService;
import com.blockeng.admin.service.CoinService;
import com.blockeng.admin.service.CoinTypeService;
import com.google.common.base.Strings;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 币种类型 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-22
 */
@RestController
@RequestMapping("/coinType")
@Api(value = "币种类型", description = "币种类型管理")
public class CoinTypeController {

    @Autowired
    private CoinTypeService coinTypeService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private CoinConfigService coinConfigService;

    @Log(value = "按条件分页查询币种类型列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "code", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态:0 禁用 1启用", required = false, dataType = "String", paramType = "query"),
    })
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "按条件分页查询币种类型列表", httpMethod = "GET")
    public Object selectPage(@ApiIgnore Page<CoinType> page,
                             String id, String code, String status) {
        EntityWrapper<CoinType> ew = new EntityWrapper<>();
        if (!Strings.isNullOrEmpty(id)) {
            ew.eq("id", id);
        }
        if (!Strings.isNullOrEmpty(code)) {
            ew.like("code", code);
        }
        if (!Strings.isNullOrEmpty(status)) {
            ew.eq("status", status);
        }
        ew.orderBy("created", false);
        return ResultMap.getSuccessfulResult(coinTypeService.selectPage(page, ew));
    }

    @Log(value = "所有币种类型列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    @GetMapping("/all")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "所有币种类型列表", httpMethod = "GET")
    @ApiImplicitParam(name = "status", value = "状态:0 禁用 1启用", required = false, dataType = "String", paramType = "query")
    public Object selectAll(String status) {
        EntityWrapper<CoinType> ew = new EntityWrapper<CoinType>();
        if (StringUtils.isNotBlank(status)) {
            ew.eq("status", status);
        }
        return ResultMap.getSuccessfulResult(coinTypeService.selectList(ew));
    }

    @Log(value = "币种类型信息", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种类型信息", httpMethod = "GET")
    public Object selectOne(@PathVariable String id) {
        return ResultMap.getSuccessfulResult(coinTypeService.selectById(id));
    }

    @Log(value = "新增币种类型", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('trade_coin_type_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增币种类型", httpMethod = "POST")
    public Object insert(@RequestBody CoinType coinType) {
        if (Strings.isNullOrEmpty(coinType.getCode())) {
            return ResultMap.getFailureResult("请填写CODE!");
        }
        CoinType dataObj = coinTypeService.selectOne(
                new EntityWrapper<CoinType>().eq("code", coinType.getCode().trim()));
        if (null != dataObj) {
            return ResultMap.getFailureResult("该CODE已存在!");
        }
        if (coinTypeService.insert(coinType)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "修改币种类型", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改币种类型")
    @RequestMapping(method = RequestMethod.PUT)
    public Object update(@RequestBody CoinType coinType) {
        if (null == coinType.getId() || coinType.getId() <= 0) {
            return ResultMap.getFailureResult("请填写ID!");
        }
        if (Strings.isNullOrEmpty(coinType.getCode())) {
            return ResultMap.getFailureResult("请填写CODE!");
        }
        CoinType dataObj = coinTypeService.selectOne(new EntityWrapper<CoinType>()
                .eq("code", coinType.getCode().trim()));
        if (null != dataObj && !dataObj.getId().equals(coinType.getId())) {
            return ResultMap.getFailureResult("该CODE已存在!");
        }
        if (coinTypeService.updateById(coinType)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }

    }

    @Log(value = "删除币种类型", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('trade_coin_type_delete')")
    @RequestMapping({"/delete"})
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除币种类型", httpMethod = "POST")
    public Object delete(@RequestBody String[] ids) {
        if (null == ids || ids.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        List<CoinType> coinTypeList = coinTypeService.selectList(new EntityWrapper<CoinType>().in("id", ids));
        String[] codeList = new String[coinTypeList.size()];
        int t = 0;
        for (CoinType ct : coinTypeList) {
            codeList[t] = ct.getCode();
            t++;
        }
        List<Coin> coins = coinService.selectList(new EntityWrapper<Coin>().in("type", codeList));
        if (coins != null && coins.size() > 0) {
            String[] codes = new String[coins.size()];
            for (int i = 0; i < coins.size(); i++) {
                codes[i] = coins.get(i).getType();
            }
            return ResultMap.getFailureResult("所选币种类型" + codes + "已在使用，不能进行删除！");
        }
        List<CoinConfig> coinConfigs = coinConfigService.selectList(new EntityWrapper<CoinConfig>().in("coin_type", codeList));
        if (coinConfigs != null && coinConfigs.size() > 0) {
            String[] codes = new String[coinConfigs.size()];
            for (int i = 0; i < coins.size(); i++) {
                codes[i] = coinConfigs.get(i).getCoinType();
            }
            return ResultMap.getFailureResult("所选币种类型" + codes + "已在使用，不能进行删除！");
        }
        if (coinTypeService.deleteBatchIds(Arrays.asList(ids))) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "币种类型状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种类型状态设置(status:1启用 0禁用)")
    @RequestMapping(path = "/setStatus", method = RequestMethod.POST)
    public Object setStatus(@RequestBody CoinType coinType) {
        CoinType oriCoinType = coinTypeService.selectById(coinType.getId());
        if (null == oriCoinType) {
            return ResultMap.getFailureResult("该记录不存在!");
        }
        if (coinType.getStatus() > 1
                || coinType.getStatus() < 0
                || coinType.getStatus() == null) {
            return ResultMap.getFailureResult("输入参数有误!");
        }
        if (coinType.getStatus().equals(oriCoinType.getStatus())) {
            return ResultMap.getFailureResult("当前状态不能执行此操作!");
        }
        if (coinType.getStatus().equals(CommonUtils.STATUS_0)) {
            Coin coins = coinService.selectOne(new EntityWrapper<Coin>().in("type", oriCoinType.getCode()));
            if (coins != null) {
                return ResultMap.getFailureResult("所选币种类型" + oriCoinType.getCode() + "已在使用，不能进行禁用！");
            }
            CoinConfig coinConfigs = coinConfigService.selectOne(new EntityWrapper<CoinConfig>().in("coin_type", oriCoinType.getCode()));
            if (coinConfigs != null) {
                return ResultMap.getFailureResult("所选币种类型" + oriCoinType.getCode() + "已在使用，不能进行删除！");
            }
        }
        CoinType newCoinType = new CoinType();
        newCoinType.setId(coinType.getId());
        newCoinType.setStatus(coinType.getStatus());
        if (coinTypeService.updateById(newCoinType)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    @Log(value = "币种类型状态设置_批量", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "币种类型状态设置_批量")
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
        List<CoinType> coinTypeList = coinTypeService.selectList(new EntityWrapper<CoinType>().in("id", ids));
        List<CoinType> newCoinList = new ArrayList<>();
        if (coinTypeList.size() < 1) {
            return ResultMap.getFailureResult("记录为空!");
        }
        CoinType newCoinType;
        for (CoinType coinType : coinTypeList) {
            if (coinType.getStatus().equals(status)) {
                return ResultMap.getFailureResult("id:[" + coinType.getId() + "]当前状态不能执行此操作,请刷新重试!");
            }
            newCoinType = new CoinType();
            newCoinType.setId(coinType.getId());
            newCoinType.setStatus(status);
            newCoinList.add(newCoinType);
        }
        if (coinTypeService.updateBatchById(newCoinList)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }
}
