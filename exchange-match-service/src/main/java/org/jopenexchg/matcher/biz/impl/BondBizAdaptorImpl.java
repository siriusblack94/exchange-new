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
package org.jopenexchg.matcher.biz.impl;

import com.blockeng.model.Order;
import org.jopenexchg.matcher.OrderType;
import org.jopenexchg.matcher.biz.BizAdaptor;

import java.math.BigDecimal;

public final class BondBizAdaptorImpl implements BizAdaptor {
    final static long MAX_PRIOR_ADJUST = 0;
    final static long PRIOR_SHIFT = 0;

    private final BigDecimal calcBasePrior(OrderType type, BigDecimal price) {
        BigDecimal basePrior = BigDecimal.ZERO;

        if (type.equals(OrderType.BUY)) {
            basePrior = basePrior.subtract(price);
        } else {
            basePrior = price;
        }

        return basePrior;
    }

    @Override
    public final BigDecimal calcPrior(Order order) {
        return calcBasePrior(order.getSide(), order.getPrice());
    }

    /**
     * 这里的实现并无出奇之处，但如果有特别的需求，则可以在移位的基础上加上调节量
     */
    @Override
    public final BigDecimal calcMaxPrior(OrderType type, BigDecimal price) {
        return calcBasePrior(type, price);
    }

    @Override
    public final BigDecimal ordPrc2Price(BigDecimal ordPrc) {
        return BigDecimal.valueOf(10000).subtract(ordPrc);
    }

    @Override
    public final BigDecimal price2OrdPrc(BigDecimal price) {
        // TODO Auto-generated method stub

        return BigDecimal.valueOf(10000).subtract(price);
    }

}
