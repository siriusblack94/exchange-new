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
package org.jopenexchg.matcher.event.impl;

import com.blockeng.model.Order;
import org.jopenexchg.matcher.event.EventHandler;

import java.math.BigDecimal;

/**
 * @author qiang
 */
public final class EventHandlerDebugImpl implements EventHandler {

    @Override
    public void enterOrderBook(final Order order) {
        System.out.println("");
        System.out.println("[enterOrderBook.将订单加入订单薄]");
        System.out.println(" + " + order);
    }

    @Override
    public void leaveOrderBook(final Order order) {
        System.out.println("");
        System.out.println("[leaveOrderBook.将订单移出订单薄]");
        System.out.println(" - " + order);
    }

    @Override
    public void match(final Order newOrder, final Order oldOrder, BigDecimal matchQty, BigDecimal matchPrice) {
        System.out.println("");
        System.out.println("[match.撮合成功]: matchPrice = " + matchPrice + " matchQty = " + matchQty);
        System.out.println("     new " + newOrder);
        System.out.println("     old " + oldOrder);
    }

    @Override
    public void noMoreMatch(final Order order) {
        System.out.println("");
        System.out.println("[noMoreMatch.匹配不成功的订单]");
        System.out.println(" + " + order);
    }


    @Override
    public void incomingOrder(final Order order) {
        System.out.println("------------------------");
        System.out.println("[incomingOrder.入队列的订单]");
        System.out.println(" + " + order);
    }


    @Override
    public void callAuctionMatch(final Order buyOrder, final Order sellOrder, BigDecimal matchQty, BigDecimal matchPrice) {
        System.out.println("");
        System.out.println("[ocall match]: matchPrice = " + matchPrice + " matchQty = " + matchQty);
        System.out.println("     buy " + buyOrder);
        System.out.println("     sell " + sellOrder);
    }
}
