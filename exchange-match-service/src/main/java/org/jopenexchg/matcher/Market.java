/* Java Open Exchange(jOpenExchg) Project
 *
 * Copyright (C) 2013  Alex Song
 *
 * This file is part of jOpenExchg.
 *
 * jOpenExchg is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * jOpenExchg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jopenexchg.matcher;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author qiang
 */
public final class Market {

    static final int UL_PRICE = Integer.MAX_VALUE;
    static final int LL_PRICE = 0;
    static final BigDecimal NO_PRICE = BigDecimal.ZERO;

    // 交易市场标识符
    public String symbol;

    // 交易市场简称
    public byte marketName[] = null;

    // 买入的价格队列. Long 是优先级而非价格
    public TreeMap<BigDecimal, PriceLeader> bids = null;

    // 卖出的价格队列. Long 是优先级而非价格
    public TreeMap<BigDecimal, PriceLeader> asks = null;

    // 最新行情情况. 都是根据订单的 ordPrc 字段而非 price 字段来的
    public BigDecimal prevClsPrc = NO_PRICE;
    public BigDecimal openPrc = NO_PRICE;
    public long highPrc = LL_PRICE;
    public long lowPrc = UL_PRICE;

    // 总成交量和成交金额
    public long totalValue = 0;
    public long totalAmount = 0;

    public Market(String symbol) {
        this.symbol = symbol;
        bids = new TreeMap<BigDecimal, PriceLeader>();
        asks = new TreeMap<BigDecimal, PriceLeader>();
    }

    // 获得本方价位列表树
    public final TreeMap<BigDecimal, PriceLeader> getPrcList(OrderType type) {
        if (type.equals(OrderType.BUY)) {
            return bids;
        } else {
            return asks;
        }
    }

    // 获得对手方价位列表树
    public final TreeMap<BigDecimal, PriceLeader> getPeerPrcTree(OrderType type) {
        if (!type.equals(OrderType.BUY)) {
            return bids;
        } else {
            return asks;
        }
    }

    // 向对应价位列表树上添加一个价位
    public final void addtoPrcList(OrderType type, PriceLeader prcLdr) {
        if (type.equals(OrderType.BUY)) {
            bids.put(prcLdr.score, prcLdr);
        } else {
            asks.put(prcLdr.score, prcLdr);
        }
    }


    /**
     * 获取最优的对手方价位
     *
     * @param type: 本方是不是买
     * @return null when does not exist such a peer prcldr
     */
    public final Map.Entry<BigDecimal, PriceLeader> getBestPeerPrcLdr(OrderType type) {
        Map.Entry<BigDecimal, PriceLeader> bestPrcLdr = null;
        TreeMap<BigDecimal, PriceLeader> prcList = getPeerPrcTree(type);

        bestPrcLdr = prcList.firstEntry();

        return bestPrcLdr;
    }
}