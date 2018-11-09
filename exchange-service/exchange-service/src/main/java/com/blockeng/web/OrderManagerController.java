package com.blockeng.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.DealRecordDTO;
import com.blockeng.entity.EntrustOrder;
import com.blockeng.entity.TurnoverOrder;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.security.UserDetails;
import com.blockeng.service.EntrustOrderService;
import com.blockeng.service.TurnoverOrderService;
import com.blockeng.vo.OrderForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 订单管理控制类
 * by crow
 * 2018年5月14日18:31:05
 */
@RestController
@RequestMapping("/orderManager")
@Slf4j
@Api(value = "数字货币-订单管理", description = "数字货币-订单管理")
public class OrderManagerController {

    @Autowired
    private EntrustOrderService entrustOrderService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    /**
     * 订单管理-委托记录
     *
     * @param form        查询条件
     * @param userDetails 当前登录用户
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/entrustOrder")
    @ApiOperation(value = "ORDER-001 委托记录", notes = "委托记录", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    public Object entrustOrder(@RequestBody OrderForm form,
                               @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        QueryWrapper<EntrustOrder> e = new QueryWrapper<>();
        e.eq("user_id", userDetails.getId());
        if (!StringUtils.isEmpty(form.getSymbol()) && !"0".equals(form.getSymbol())) {
            e.eq("symbol", form.getSymbol());
        }
        if (form.getType() != 0) {
            e.eq("type", form.getType());
        }
        e.orderByDesc("id");
        IPage<EntrustOrder> page = new Page<>(form.getCurrent(), form.getSize());
        IPage<EntrustOrder> orders = entrustOrderService.selectPage(page, e);
        return Response.ok(orders);
    }

    /**
     * 订单管理-成交记录
     *
     * @param form        查询条件
     * @param userDetails 当前登录用户
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/dealOrder")
    @ApiOperation(value = "ORDER-002 成交记录", notes = "成交记录", httpMethod = "POST"
            , authorizations = {@Authorization(value = "Authorization")})
    public Object dealOrder(@RequestBody OrderForm form,
                            @ApiIgnore @AuthenticationPrincipal UserDetails userDetails) {
        if (!Optional.ofNullable(userDetails).isPresent()) {
            throw new GlobalDefaultException(1000);
        }
        IPage<TurnoverOrder> page = new Page<>(form.getCurrent(), form.getSize());
        QueryWrapper<TurnoverOrder> ew = new QueryWrapper<>();
        if (!StringUtils.isEmpty(form.getSymbol()) && !"0".equalsIgnoreCase(form.getSymbol())) {
            ew.eq("symbol", form.getSymbol());
        }
        if (form.getType() <= 0) {//查询全部
            ew.and(i -> i.eq("sell_user_id", userDetails.getId()).or().eq("buy_user_id", userDetails.getId()));
        } else if (form.getType() == 1) {//买入
            ew.eq("buy_user_id", userDetails.getId());
        } else if (form.getType() == 2) {//卖出
            ew.eq("sell_user_id", userDetails.getId());
        } else {
            return Response.err(50067, "错误的类型");
        }
        IPage<TurnoverOrder> pages = turnoverOrderService.selectOrders(page, ew);
        List<TurnoverOrder> records = pages.getRecords();
        List<DealRecordDTO> dealList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(records)) {
            for (TurnoverOrder item : records) {//成交记录
                DealRecordDTO dealRecordDTO = new DealRecordDTO();
                dealList.add(dealRecordDTO);
                dealRecordDTO.setCreateTime(item.getCreated())
                        .setTurnoverAmount(item.getAmount().stripTrailingZeros())
                        .setTurnoverNum(item.getVolume().stripTrailingZeros())
                        .setMarketName(item.getMarketName())
                        .setTurnoverPrice(item.getPrice().stripTrailingZeros());
                if (item.getBuyUserId().equals(item.getSellUserId())) { //自买自卖
                    dealRecordDTO.setDealBuyFee(item.getDealBuyFee().stripTrailingZeros());
                    dealRecordDTO.setDealSellFee(item.getDealSellFee().stripTrailingZeros());
                    dealRecordDTO.setType(form.getType());
                } else if (item.getBuyUserId().equals(userDetails.getId())) {//买
                    dealRecordDTO.setDealBuyFee(item.getDealBuyFee().stripTrailingZeros());
                    dealRecordDTO.setType(1);
                } else {
                    dealRecordDTO.setDealSellFee(item.getDealSellFee().stripTrailingZeros());
                    dealRecordDTO.setType(2);
                }
            }
        }
        return Response.ok(new Page<DealRecordDTO>().setRecords(dealList).
                setTotal(pages.getTotal()).
                setCurrent(pages.getCurrent()).
                setSize(pages.getSize()));
    }
}
