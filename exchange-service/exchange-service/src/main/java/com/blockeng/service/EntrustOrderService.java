package com.blockeng.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.CreateTradeOrderDTO;
import com.blockeng.dto.DepthsDTO;
import com.blockeng.dto.EntrustOrderMatchDTO;
import com.blockeng.dto.TradeEntrustOrderDTO;
import com.blockeng.entity.EntrustOrder;
import com.blockeng.entity.Market;
import com.blockeng.framework.enums.OrderType;
import com.blockeng.framework.exception.GlobalDefaultException;

import java.math.BigDecimal;

/**
 * <p>
 * 委托订单信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface EntrustOrderService extends IService<EntrustOrder> {

    /**
     * 查询未完成委托订单
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     * @param page   查询条件及分页参数
     * @return
     */
    IPage<TradeEntrustOrderDTO> queryEntrustOrder(String symbol, long userId, IPage<EntrustOrder> page);

    /**
     * 查询历史委托订单
     *
     * @param symbol 交易对标识符
     * @param userId 用户ID
     * @param page   查询条件及分页参数
     * @return
     */
    IPage<TradeEntrustOrderDTO> queryHistoryEntrustOrder(String symbol, long userId, IPage<EntrustOrder> page);

    /**
     * 查询深度
     *
     * @param symbol
     * @param mod
     * @return
     */
    DepthsDTO queryDepths(String symbol, BigDecimal mod);
}