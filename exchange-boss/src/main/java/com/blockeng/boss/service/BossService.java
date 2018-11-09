package com.blockeng.boss.service;

import com.blockeng.boss.dto.DealOrder;
import com.blockeng.boss.dto.EntrustOrderMatchDTO;
import com.blockeng.framework.exception.ExchangeException;

/**
 * @Description: 资金清算
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午12:09
 * @Modified by: Chen Long
 */
public interface BossService {

    /**
     * 结算
     *
     * @param dealOrder
     */
    boolean settlement(DealOrder dealOrder) throws ExchangeException;

    boolean cancel(EntrustOrderMatchDTO dealOrder);
}
