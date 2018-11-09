package com.blockeng.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.TurnoverData24HDTO;
import com.blockeng.dto.TurnoverDataDTO;
import com.blockeng.entity.TurnoverOrder;

import java.math.BigDecimal;

/**
 * <p>
 * 成交订单 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface TurnoverOrderService extends IService<TurnoverOrder> {

    /**
     * 获取交易对最新成交价
     *
     * @param marketId 交易对ID
     * @return
     */
    BigDecimal queryCurrentPrice(Long marketId);

    /**
     * 获取24小时成交数据
     *
     * @param symbol 交易对
     * @return
     */
    TurnoverData24HDTO query24HDealData(String symbol);

    IPage<TurnoverOrder> selectOrders(IPage<TurnoverOrder> page, Wrapper<TurnoverOrder> wrapper);
}
