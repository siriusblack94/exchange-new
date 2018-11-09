package com.blockeng.trade.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.framework.exception.GlobalDefaultException;
import com.blockeng.trade.dto.CreateTradeOrderDTO;
import com.blockeng.trade.dto.EntrustOrderMatchDTO;
import com.blockeng.trade.dto.TradeDealDTO;
import com.blockeng.trade.entity.EntrustOrder;
import com.blockeng.trade.entity.Market;

/**
 * @Description: 订单接口
 * @Author: Chen Long
 * @Date: Created in 2018/7/18 下午10:22
 * @Modified by: Chen Long
 */
public interface EntrustOrderService extends IService<EntrustOrder> {

    /**
     * 创建委托下单
     *
     * @param market 交易市场
     * @param order  委托下单参数
     * @param userId 当前登录用户
     * @return
     */
    EntrustOrder createEntrustOrder(Market market, CreateTradeOrderDTO order, Long userId);

    /**
     * 币币交易撤销委托
     *
     * @param cancelOrder 待撤销委托订单
     */
    boolean cancelEntrustOrder(EntrustOrderMatchDTO cancelOrder) throws GlobalDefaultException;

    /**
     * 机器人刷单接口（自成交）
     *
     * @param market 交易市场
     * @param order  创建订单请求参数
     * @param userId 用户ID
     */
    void createOrder(Market market, TradeDealDTO order, Long userId);

    /**
     * 开始撤销
     *
     * @param orderId
     */
    void startCancel(Long orderId);
}
