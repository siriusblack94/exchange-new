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
package org.jopenexchg.matcher.biz;

import com.blockeng.model.Order;
import org.jopenexchg.matcher.OrderType;

import java.math.BigDecimal;

/**
 * 本接口用来封装撮合器需要调用、然而和业务相关的函数, 令其和撮合器逻辑分离
 * <p>
 * 包括不限于：计算订单优先级；计算成交金额等
 * <p>
 * 优先级的设置方面：数字越小意味着越先被考虑
 * 优先级设置思路是：
 * 对于期权卖盘，同一价格下平仓优先可以视作把卖价略为调低
 * 对于期权买盘，同一价格下平仓优先可以视作把买价略为调高
 * <p>
 * 做法是引入调整因子 adjust. 普通的是0，需要增加优先级的，adjust > 0
 * 对于卖盘：
 * prior = (price << N - adjust)
 * 对于买盘：
 * prior = -(price << N + adjust)
 * <p>
 * 这样，adjust 的合法区间就是 [0, 2^N -1] 了
 */

public interface BizAdaptor {
    /**
     * 计算订单优先级
     *
     * @return
     */
    public BigDecimal calcPrior(Order order);


    /**
     * 计算此价位下的最大优先级的值
     *
     * @return
     */
    public BigDecimal calcMaxPrior(OrderType type, BigDecimal price);

    /**
     * 根据order里面的原始报价计算内部使用的price
     */
    public BigDecimal ordPrc2Price(BigDecimal ordPrc);

    public BigDecimal price2OrdPrc(BigDecimal price);

}
