package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.TradeEntrustOrderDTO;
import com.blockeng.dto.TurnoverDataDTO;
import com.blockeng.entity.EntrustOrder;
import com.blockeng.framework.constants.Constant;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.EntrustOrderService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description: 币币交易订单 controller
 * @Author: Chen Long
 * @Date: Created in 2018/5/14 下午8:00
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/trade/order")
@Slf4j
@Api(value = "币币交易订单", description = "币币交易订单 REST API")
public class TradeOrderController implements Constant {

    @Autowired
    private EntrustOrderService entrustOrderService;

    /**
     * 根据订单号查询订单
     *
     * @param orderId 订单号
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/entrusts/{orderId}")
    @ApiOperation(value = "根据订单号查询委托订单", notes = "根据订单号查询委托订单", httpMethod = "GET",
            authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParam(name = "orderId", value = "币币交易委托订单号", required = true, dataType = "long", paramType = "path")
    @ResponseBody
    public Object getEntrustOrder(@PathVariable("orderId") long orderId,
                                  @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (orderId <= 0L) {
            log.error("查询订单失败，订单号错误，orderId：{}", orderId);
            throw new GlobalDefaultException(50008);
        }
        QueryWrapper<EntrustOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderId).eq("user_id", userDetails.getId());
        return Response.ok(entrustOrderService.selectOne(wrapper));
    }

    /**
     * 查询未完成委托
     *
     * @param symbol  交易对标识符
     * @param size    每页显示数据量
     * @param current 当前页码
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/entrusts/{symbol}/{current}/{size}")
    @ApiOperation(value = "ORDER-003 币币交易未完成委托订单", notes = "币币交易未完成委托订单", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示数据量", required = true, dataType = "int", paramType = "path")
    })
    @ResponseBody
    public Object entrustOrder(@PathVariable("symbol") String symbol,
                               @PathVariable("current") int current,
                               @PathVariable("size") int size,
                               @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        IPage<EntrustOrder> page = new Page<>();
        page.setCurrent(current).setSize(size);
        IPage<TradeEntrustOrderDTO> entrustOrderList = entrustOrderService.queryEntrustOrder(symbol, userDetails.getId(), page);
        return Response.ok(entrustOrderList);
    }

    /**
     * 查询历史委托
     *
     * @param symbol  交易对标识符
     * @param size    每页显示数据量
     * @param current 当前页码
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/history/{symbol}/{current}/{size}")
    @ApiOperation(value = "ORDER-004 币币交易历史委托订单", notes = "币币交易历史委托订单", httpMethod = "GET", authorizations = {@Authorization(value = "Authorization")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "symbol", value = "交易对标识符", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页显示数据量", required = true, dataType = "int", paramType = "path")
    })
    @ResponseBody
    public Object historyOrder(@PathVariable("symbol") String symbol,
                               @PathVariable("current") int current,
                               @PathVariable("size") int size,
                               @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        IPage<EntrustOrder> page = new Page<>();
        page.setCurrent(current).setSize(size);
        IPage<TradeEntrustOrderDTO> entrustOrderList = entrustOrderService.queryHistoryEntrustOrder(symbol, userDetails.getId(), page);
        return Response.ok(entrustOrderList);
    }
}
