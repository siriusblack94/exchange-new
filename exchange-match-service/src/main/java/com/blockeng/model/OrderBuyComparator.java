package com.blockeng.model;

import java.util.Comparator;

/**
 * 买单比较器
 */
public class OrderBuyComparator implements Comparator<Order> {

    //价格优先,然后时间优先
    @Override
    public int compare(Order o1, Order o2) {
        if (o1.getPrice().compareTo(o2.getPrice()) > 0) {
            return -1;
        } else if (o1.getPrice().compareTo(o2.getPrice()) < 0) {
            return 1;
        } else {
            if (o1.getCreated().compareTo(o2.getCreated()) == 1) {
                return -1;
            } else if (o1.getCreated().compareTo(o2.getCreated()) == 0) {
                // return 0;
                return -o1.getId().compareTo(o2.getId());
            } else {
                return 1;
            }
        }
    }
}
