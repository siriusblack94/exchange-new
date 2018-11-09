package com.blockeng.mining.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.mining.entity.TurnoverOrder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 成交订单 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface TurnoverOrderService extends IService<TurnoverOrder> {
    IPage<TurnoverOrder> selectOrders(IPage<TurnoverOrder> page, Wrapper<TurnoverOrder> wrapper);
    List<TurnoverOrder> getOrders(String startDate, String endDate);

    List<String> getBuyUser();
    List<String> getSellUser();
    List<TurnoverOrder> getOrderByUseridByDay(String startDate, String endDate,String userid);
}
