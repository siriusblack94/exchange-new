package com.blockeng.data;

import com.blockeng.model.Order;
import com.blockeng.model.OrderBuyComparator;
import com.blockeng.model.OrderSellComparator;
import org.aspectj.weaver.ast.Or;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;

public class MarketData {

    /**
     * 买单队列
     */
    public PriorityQueue<Order> buyQueue = new PriorityQueue(new OrderBuyComparator());

    /**
     * 卖单队列
     */
    public PriorityQueue<Order> sellQueue = new PriorityQueue(new OrderSellComparator());
}
