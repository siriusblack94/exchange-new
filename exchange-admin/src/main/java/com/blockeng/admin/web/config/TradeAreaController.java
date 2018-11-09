package com.blockeng.admin.web.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.annotation.Log;
import com.blockeng.admin.common.CommonUtils;
import com.blockeng.admin.common.ResultMap;
import com.blockeng.admin.dto.UpdateTradeAreaStatusDTO;
import com.blockeng.admin.entity.Market;
import com.blockeng.admin.entity.TradeArea;
import com.blockeng.admin.enums.SysLogTypeEnum;
import com.blockeng.admin.service.MarketService;
import com.blockeng.admin.service.TradeAreaService;
import com.blockeng.framework.enums.TradeAreaType;
import com.blockeng.framework.exception.ExchangeException;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 交易区 前端控制器
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@RestController
@RequestMapping("/trade/area")
@Api(value = "币币交易区域", description = "币币交易区域")
public class TradeAreaController {

    @Autowired
    private TradeAreaService tradeAreaService;

    @Autowired
    private MarketService marketService;

    /**
     * 分页查询币币交易区域列表
     *
     * @param page
     * @param name
     * @param status
     * @return
     */
    @Log(value = "分页查询币币交易区域列表", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_area_query')")
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "每页显示条数，默认 10", dataType = "String", paramType = "query", defaultValue = "10"),
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "String", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "name", value = "区域名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态：1：启用；0-禁用；空：全部", dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "分页查询币币交易区域列表", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ResponseBody
    public Object selectPage(@ApiIgnore Page<TradeArea> page,
                             @RequestParam(value = "name", required = false) String name,
                             @RequestParam(value = "status", required = false) Integer status) {
        return ResultMap.getSuccessfulResult(tradeAreaService.queryPage(page, TradeAreaType.DC_TYPE, name, status));
    }

    /**
     * 查看交易区域详情
     *
     * @param id 交易区域ID
     * @return
     */
    @Log(value = "查看交易区域详情", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_area_query')")
    @GetMapping("/{id}")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "查看交易区域详情", httpMethod = "GET")
    @ResponseBody
    public Object selectOne(@PathVariable String id) {
        return ResultMap.getSuccessfulResult(tradeAreaService.selectById(id));
    }

    /**
     * 查询所有币币交易区域
     *
     * @return
     */
    @Log(value = "查询所有币币交易区域", type = SysLogTypeEnum.SELECT)
    @PreAuthorize("hasAuthority('trade_area_query')")
    @GetMapping("/all")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "查询所有币币交易区域", httpMethod = "GET")
    @ApiImplicitParam(name = "status", value = "状态:0 禁用 1启用", required = false, dataType = "String", paramType = "query")
    @ResponseBody
    public Object selectAll(String status) {
        return ResultMap.getSuccessfulResult(tradeAreaService.queryByType(TradeAreaType.DC_TYPE, status));
    }

    /**
     * 新增币币交易区域
     *
     * @param tradeArea
     * @return
     */
    @Log(value = "新增币币交易区域", type = SysLogTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('trade_area_create')")
    @PostMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "新增币币交易区域", httpMethod = "POST")
    @ResponseBody
    public Object insert(@RequestBody TradeArea tradeArea) {
        tradeArea.setType(TradeAreaType.DC_TYPE.getCode());
        if (tradeArea != null) {
            TradeArea tradeArea1 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("name", tradeArea.getName()).eq("type", TradeAreaType.DC_TYPE.getCode()));
            if (tradeArea1 != null) {
                return ResultMap.getSuccessfulResult("区域名称已存在，请换个名称!");
            }
            TradeArea tradeArea2 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("code", tradeArea.getCode()).eq("type", TradeAreaType.DC_TYPE.getCode()));
            if (tradeArea2 != null) {
                return ResultMap.getSuccessfulResult("区域编号已存在，请换个编号!");
            }
            if (tradeArea.getBaseCoin() == CommonUtils.BASE_COIN_1) {
                TradeArea tradeArea3 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("base_coin", CommonUtils.BASE_COIN_1).eq("type", TradeAreaType.DC_TYPE.getCode()));
                if (tradeArea3 != null) {
                    return ResultMap.getFailureResult("结算币种只能只有一个，请把之前的资产统计标识改成否，再修改。");
                }
            }
        }
        if (tradeAreaService.insert(tradeArea)) {
            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    /**
     * 修改币币交易区域
     *
     * @param tradeArea
     * @return
     */
    @Log(value = "修改币币交易区域", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_area_update')")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "修改币币交易区域")
    @PutMapping
    @ResponseBody
    public Object update(@RequestBody TradeArea tradeArea) {
        tradeArea.setType(TradeAreaType.DC_TYPE.getCode());
        TradeArea trade = tradeAreaService.selectById(tradeArea.getId());
        if (trade != null && !trade.getName().equals(tradeArea.getName())) {
            TradeArea tradeArea1 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("name", tradeArea.getName()).eq("type", TradeAreaType.DC_TYPE.getCode()));
            if (tradeArea1 != null) {
                return ResultMap.getSuccessfulResult("区域名称已存在，请换个名称!");
            }
        }
        if (trade != null && !trade.getCode().equals(tradeArea.getCode())) {
            TradeArea tradeArea2 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("code", tradeArea.getCode()).eq("type", TradeAreaType.DC_TYPE.getCode()));
            if (tradeArea2 != null) {
                return ResultMap.getSuccessfulResult("区域编号已存在，请换个编号!");
            }
        }
        if (trade != null && tradeArea.getBaseCoin() == CommonUtils.BASE_COIN_1 && trade.getBaseCoin() == CommonUtils.BASE_COIN_0) {
            TradeArea tradeArea3 = tradeAreaService.selectOne(new EntityWrapper<TradeArea>().eq("base_coin", CommonUtils.BASE_COIN_1).eq("type", TradeAreaType.DC_TYPE.getCode()));
            if (tradeArea3 != null && tradeArea.getBaseCoin() == CommonUtils.BASE_COIN_1) {
                return ResultMap.getFailureResult("结算币种只能只有一个，请把之前的资产统计标识改成否，再修改。");
            }
        }
        if (tradeAreaService.updateById(tradeArea)) {

            return ResultMap.getSuccessfulResult("操作成功!");
        } else {
            return ResultMap.getFailureResult("操作失败!");
        }
    }

    /**
     * 修改交易区域状态
     *
     * @param updateTradeAreaStatus
     * @return
     */
    @Log(value = "交易区状态设置", type = SysLogTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('trade_area_update')")
    @PutMapping("/status")
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "交易区状态设置(status：1-启用；0-禁用)")
    @ResponseBody
    public Object setStatus(@RequestBody UpdateTradeAreaStatusDTO updateTradeAreaStatus) {
        TradeArea oriTradeArea = tradeAreaService.selectById(updateTradeAreaStatus.getId());
        if (null == oriTradeArea) {
            return ResultMap.getFailureResult("该记录不存在!");
        }
        if (updateTradeAreaStatus.getStatus() > 1
                || updateTradeAreaStatus.getStatus() < 0) {
            return ResultMap.getFailureResult("输入参数有误!");
        }
        if (updateTradeAreaStatus.getStatus().equals(CommonUtils.STATUS_0)) {
            Market market = marketService.selectOne(new EntityWrapper<Market>().eq("trade_area_id", updateTradeAreaStatus.getId()));
            if (market != null) {
                return ResultMap.getFailureResult("所选交易区域" + updateTradeAreaStatus.getId() + "已在使用，不能进行禁用！");
            }
        }
        oriTradeArea.setStatus(updateTradeAreaStatus.getStatus());
        tradeAreaService.updateById(oriTradeArea);
        return ResultMap.getSuccessfulResult("操作成功!");
    }

    /**
     * 删除交易区域
     *
     * @param id 交易区域id数组
     * @return
     */
    @Log(value = "删除交易区域", type = SysLogTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('trade_area_delete')")
    @DeleteMapping
    @ApiOperation(authorizations = {@Authorization(value = "Authorization")}, value = "删除交易区域", httpMethod = "DELETE")
    @ApiImplicitParam(name = "id", value = "交易区域id数组", required = true)
    @ResponseBody
    public ResultMap delete(@RequestBody String[] id) {
        if (null == id || id.length <= 0) {
            return ResultMap.getFailureResult("参数不能为空！");
        }
        try {
            List<Market> marketList = marketService.selectList(new EntityWrapper<Market>().in("trade_area_id", id));
            if (marketList != null && marketList.size() > 0) {
                Long[] codes = new Long[marketList.size()];
                for (int i = 0; i < marketList.size(); i++) {
                    codes[i] = marketList.get(i).getTradeAreaId();
                }
                return ResultMap.getFailureResult("所选交易区域" + codes + "已在使用，不能进行删除！");
            }
            tradeAreaService.batchDelete(Arrays.asList(id));
            return ResultMap.getSuccessfulResult("操作成功");
        } catch (ExchangeException e) {
            return ResultMap.getFailureResult(e.getMessage());
        }
    }
}
