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
 *
 *
 * Modify History:
 * 2014-02-22   Alex Song
 * 		Modify the open auction algorithm to reflect the correct
 * 		understanding of the phrase "the remain quantity should be minimal"
 *
 * 2014-05-19   Alex Song
 *      Implemented a newly found simple algorithm for open auction
 */
package org.jopenexchg.matcher;

import com.blockeng.model.Order;
import org.jopenexchg.matcher.biz.BizAdaptor;
import org.jopenexchg.matcher.event.EventHandler;
import org.jopenexchg.pool.AllocOnlyPool;
import org.jopenexchg.pool.RecyclablePool;

import java.math.BigDecimal;
import java.util.*;


public final class Matcher implements BizAdaptor {
    private TradedInstList marketList = null;
    private RecyclablePool<PriceLeader> prcLdrPool = null;
    private AllocOnlyPool<Order> ordrPool = null;
    private EventHandler evtCbs = null;
    private BizAdaptor bizAdpt = null;
    private LinkedList<BigDecimal> delPrcLdrList = null;
    private LinkedList<BigDecimal> delPrcLdrList2 = null;

    public Matcher(int prcLdrCnt, int orderCnt)
            throws InstantiationException, IllegalAccessException {
        bizAdpt = this;
        marketList = new TradedInstList();
        prcLdrPool = new RecyclablePool<PriceLeader>(PriceLeader.class, prcLdrCnt);
        ordrPool = new AllocOnlyPool<Order>(Order.class, orderCnt);
        delPrcLdrList = new LinkedList<BigDecimal>();
        delPrcLdrList2 = new LinkedList<BigDecimal>();

    }

    public final void setBizAdpt(BizAdaptor bizAdpt) {
        if (bizAdpt != null) {
            this.bizAdpt = bizAdpt;
        }
    }

    public final void setEvtCbs(EventHandler evtCbs) {
        this.evtCbs = evtCbs;
    }

    public final Market addMarket(String symbol) {
        Market market = new Market(symbol);
        marketList.addMarket(market);
        return market;
    }

    public final Order allocOrder() {
        return ordrPool.getObj();
    }

    /**
     * 撤单
     *
     * @return
     */
    public final boolean delOrder(Order order) {
        if (order == null) {
            return false;
        }

        if (order.getMarket() == null) {
            order.setMarket(marketList.getMarket(order.getSymbol()));
            if (order.getMarket() == null) {
                return false;
            }
        }

        order.setDelflg(true);
        BigDecimal score = bizAdpt.calcPrior(order);

        TreeMap<BigDecimal, PriceLeader> prcList = order.getMarket().getPrcList(order.getSide());
        PriceLeader prcLdr = prcList.get(score);
        if (prcLdr == null) {
            return false;
        }

        prcLdr.over = prcLdr.over.subtract(order.getOver());
        if (prcLdr.over.compareTo(BigDecimal.ZERO) <= 0) {
            prcList.remove(prcLdr.score);
            prcLdrPool.putObj(prcLdr);
        }

        if (this.evtCbs != null) {
            evtCbs.leaveOrderBook(order);
        }
        return true;
    }

    // 简单插入订单簿. 可能增加价格档位，增加已存在的价格档位的累积数量，订单簿增加订单
    private final boolean insertOrder(Order order) {
        if (order.getOver().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }

        if (order.getMarket() == null) {
            order.setMarket(marketList.getMarket(order.getSymbol()));
            if (order.getMarket() == null) {
                return false;
            }
        }

        BigDecimal score = bizAdpt.calcPrior(order);

        PriceLeader prcLdr = order.getMarket().getPrcList(order.getSide()).get(score);

        if (prcLdr == null) {
            prcLdr = prcLdrPool.getObj();
            if (prcLdr == null) {
                return false;
            }

            prcLdr.score = score;
            prcLdr.price = order.getPrice();

            order.getMarket().addtoPrcList(order.getSide(), prcLdr);
        }

        prcLdr.orderList.add(order);

        // 只有在加入单子的时候这个量才上升


        prcLdr.over = prcLdr.over.add(order.getOver());

        if (this.evtCbs != null) {
            evtCbs.enterOrderBook(order);
        }

        return true;
    }

    // 在这个里面会减少对手方  prcLdr.over
    private final boolean matchOnePrcLvl(Order newOrd, PriceLeader prcLdr, TreeMap<BigDecimal, PriceLeader> peerPrcLdrTree, Market stock) {
        Order oldOrd = null;
        BigDecimal matchQty = BigDecimal.ZERO;

        while (newOrd.getOver().compareTo(BigDecimal.ZERO) > 0) {
            oldOrd = prcLdr.orderList.peek();
            if (oldOrd == null) {
                return true;
            }

            if (oldOrd.isDelflg() == true) {
                // delayed deleting of deleted orders from list
                prcLdr.orderList.remove();
            } else {
                if (oldOrd.getOver().compareTo(newOrd.getOver()) <= 0) {
                    matchQty = oldOrd.getOver();
                    newOrd.setOver(newOrd.getOver().subtract(matchQty));
                    oldOrd.setOver(BigDecimal.ZERO);

                    prcLdr.over = prcLdr.over.subtract(matchQty);
                    prcLdr.orderList.remove();

                    if (this.evtCbs != null) {
                        evtCbs.match(newOrd, oldOrd, matchQty, prcLdr.price);

                        evtCbs.leaveOrderBook(oldOrd);
                    }
                } else {
                    matchQty = newOrd.getOver();
                    oldOrd.setOver(oldOrd.getOver().subtract(matchQty));
                    newOrd.setOver(BigDecimal.ZERO);

                    prcLdr.over = prcLdr.over.subtract(matchQty);

                    if (this.evtCbs != null) {
                        evtCbs.match(newOrd, oldOrd, matchQty, prcLdr.price);
                    }
                }
            }

        }

        return true;
    }

    // 集合竞价期间插入订单簿，不做MATCH
    public final boolean ocallInsOrder(Order order) {
        if (order.getMarket() == null) {
            order.setMarket(marketList.getMarket(order.getSymbol()));
            if (order.getMarket() == null) {
                System.out.println("getStock() failed");
                return false;
            }
        }
        order.setOver(order.getVolume());
        order.setPrice(bizAdpt.ordPrc2Price(order.getPrice()));

        if (evtCbs != null) {
            evtCbs.incomingOrder(order);
        }
        return insertOrder(order);
    }


    // 这个是连续竞价时候使用的方式，先尝试匹配再插入订单簿
    public final boolean matchInsOrder(Order order) {
        order.setOver(order.getVolume());
        order.setPrice(bizAdpt.ordPrc2Price(order.getPrice()));

        if (order.getMarket() == null) {
            order.setMarket(marketList.getMarket(order.getSymbol()));
            if (order.getMarket() == null) {
                System.out.println("getStock() failed");
                return false;
            }
        }

        if (evtCbs != null) {
            evtCbs.incomingOrder(order);
        }

        OrderType type = order.getSide() == OrderType.BUY ? OrderType.SELL : OrderType.BUY;

        BigDecimal maxPeerPrior = bizAdpt.calcMaxPrior(type, order.getPrice());

        TreeMap<BigDecimal, PriceLeader> peerPrcLdrTree = order.getMarket().getPeerPrcTree(order.getSide());
        Set<Map.Entry<BigDecimal, PriceLeader>> peerPrcLdrSet = peerPrcLdrTree.entrySet();

        long priceLevelCnt = 0;
        BigDecimal prevPrice = Market.NO_PRICE;

        Map.Entry<BigDecimal, PriceLeader> peerEntry = null;
        PriceLeader prcLdr = null;

        delPrcLdrList.clear();

        Iterator<Map.Entry<BigDecimal, PriceLeader>> its = peerPrcLdrSet.iterator();
        while (its.hasNext() && (order.getOver().compareTo(BigDecimal.ZERO) > 0)) {
            peerEntry = its.next();
            prcLdr = peerEntry.getValue();

            if (prcLdr.score.compareTo(maxPeerPrior) <= 0) {
                if (prevPrice.compareTo(prcLdr.price) != 0) {
                    priceLevelCnt++;
                    prevPrice = prcLdr.price;

                    // 在这里以后可以利用priceLevelCnt来控制市价订单吃多少档位
                    if (priceLevelCnt >= 5) {
                    }
                }

                // 针对此档位上的订单列表进行匹配
                if (false == matchOnePrcLvl(order, prcLdr, peerPrcLdrTree, order.getMarket())) {
                    System.out.println("matchOnePrcLvl() failed");
                    return false;
                }

                // 放入待回收价格档位列表
                if (prcLdr.over.compareTo(BigDecimal.ZERO) <= 0) {
                    delPrcLdrList.add(prcLdr.score);
                }
            } else {
                break;
            }
        }

        // 删除用完的对手方价格档位并回收到池中
        Iterator<BigDecimal> myIter = delPrcLdrList.iterator();
        while (myIter.hasNext()) {
            PriceLeader rmvLdr = peerPrcLdrTree.remove(myIter.next());
            prcLdrPool.putObj(rmvLdr);
        }

        if (this.evtCbs != null) {
            evtCbs.noMoreMatch(order);
        }

        if (order.getOver().compareTo(BigDecimal.ZERO) > 0) {
            if (insertOrder(order)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


    /**
     * 这里是缺省的计算订单优先级的实现
     */
    protected final BigDecimal calcBasePrior(OrderType type, BigDecimal price) {
        BigDecimal basePrior = BigDecimal.ZERO;

        if (type.equals(OrderType.BUY)) {
            basePrior = basePrior.subtract(price);
        } else {
            basePrior = price;
        }

        return basePrior;
    }

    /**
     * 以下是Matcher 对于 BizAdaptor 的自带实现. 4个函数
     */
    @Override
    public final BigDecimal calcPrior(Order order) {
        return calcBasePrior(order.getSide(), order.getPrice());
    }

    @Override
    public final BigDecimal calcMaxPrior(OrderType type, BigDecimal price) {
        return calcBasePrior(type, price);
    }


    @Override
    public final BigDecimal ordPrc2Price(BigDecimal ordPrc) {
        return ordPrc;
    }

    @Override
    public final BigDecimal price2OrdPrc(BigDecimal price) {
        return price;
    }

    /**
     * min() and max() are provided here to utilize 'final' key word
     */
    private final BigDecimal min(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) < 0) {
            return a;
        } else {
            return b;
        }
    }

    private final BigDecimal max(BigDecimal a, BigDecimal b) {
        if (a.compareTo(b) > 0) {
            return a;
        } else {
            return b;
        }
    }

    /**
     * 虚拟计算集合竞价，但不进行任何撮合匹配
     */
    public final boolean calcCallAuction(Market stock, CallAuctionResult result) {
        TreeMap<BigDecimal, PriceLeader> buyTree = null;
        TreeMap<BigDecimal, PriceLeader> sellTree = null;
        Map.Entry<BigDecimal, PriceLeader> buyEntry = null;
        Map.Entry<BigDecimal, PriceLeader> sellEntry = null;
        PriceLeader buyLdr = null;
        PriceLeader sellLdr = null;
        BigDecimal buyKey = null;
        BigDecimal sellKey = null;

        if (stock == null || result == null) {
            return false;
        }

        // TREE
        buyTree = stock.bids;
        if (buyTree == null) {
            return false;
        }

        sellTree = stock.asks;
        if (sellTree == null) {
            return false;
        }

        // LEADER
        buyEntry = buyTree.firstEntry();
        if (buyEntry == null) {
            return false;
        }
        buyKey = buyEntry.getKey();
        buyLdr = buyEntry.getValue();

        sellEntry = sellTree.firstEntry();
        if (sellEntry == null) {
            return false;
        }
        sellKey = sellEntry.getKey();
        sellLdr = sellEntry.getValue();

        if (sellLdr.price.compareTo(buyLdr.price) > 0) {
            // 订单簿完全没有交叉
            return false;
        }

        // 从买卖队列的头开始遍历
        BigDecimal totalMatchedQty = BigDecimal.ZERO;
        BigDecimal lastBuyPrice = BigDecimal.ZERO;
        BigDecimal lastSellPrice = BigDecimal.ZERO;
        BigDecimal buyQtyRemain = buyLdr.over;
        BigDecimal sellQtyRemain = sellLdr.over;

        while (buyLdr.price.compareTo(sellLdr.price) >= 0) {
            BigDecimal matchOnce = min(buyQtyRemain, sellQtyRemain);

            totalMatchedQty = totalMatchedQty.add(matchOnce);
            buyQtyRemain = buyQtyRemain.subtract(matchOnce);
            sellQtyRemain = sellQtyRemain.subtract(matchOnce);

            lastBuyPrice = buyLdr.price;
            lastSellPrice = sellLdr.price;

            if (buyQtyRemain.compareTo(BigDecimal.ZERO) == 0) {
                buyEntry = stock.bids.higherEntry(buyKey);
                if (buyEntry == null) {
                    break;
                } else {
                    buyKey = buyEntry.getKey();
                    buyLdr = buyEntry.getValue();

                    buyQtyRemain = buyLdr.over;
                }
            }

            if (sellQtyRemain.compareTo(BigDecimal.ZERO) == 0) {
                sellEntry = stock.asks.higherEntry(sellKey);
                if (sellEntry == null) {
                    break;
                } else {
                    sellKey = sellEntry.getKey();
                    sellLdr = sellEntry.getValue();

                    sellQtyRemain = sellLdr.over;
                }
            }
        }

        // get all 4 prices!
        BigDecimal headBuyPrice = BigDecimal.ZERO;
        BigDecimal headSellPrice = BigDecimal.ZERO;

        if (buyEntry == null) {
            headBuyPrice = BigDecimal.ZERO;
        } else {
            headBuyPrice = buyEntry.getValue().price;
        }

        if (sellEntry == null) {
            headSellPrice = BigDecimal.valueOf(Long.MAX_VALUE);
        } else {
            headSellPrice = sellEntry.getValue().price;
        }

        BigDecimal P1 = min(lastBuyPrice, headSellPrice);
        BigDecimal P2 = max(lastSellPrice, headBuyPrice);

        if (P1.compareTo(P2) != 0) {
            result.price = P1.add(P2).divide(BigDecimal.valueOf(2));
        } else {
            result.price = P1;
        }
        result.volume = totalMatchedQty;
        result.ordPrc = bizAdpt.price2OrdPrc(result.price);

        return true;
    }

    /**
     * 真正根据result的指示完成集合竞价
     */
    public final boolean doCallAuction(Market stock, CallAuctionResult result) {
        PriceLeader buyLdr = null;
        PriceLeader sellLdr = null;
        Order buyOrd = null;
        Order sellOrd = null;

        if (stock == null || result == null) {
            return false;
        }

        if (result.volume.compareTo(BigDecimal.ZERO) <= 0) {
            // 订单簿完全没有交叉
            return true;
        }

        BigDecimal remainQty = result.volume;

        Iterator<Map.Entry<BigDecimal, PriceLeader>> itsB = stock.bids.entrySet().iterator();
        Iterator<Map.Entry<BigDecimal, PriceLeader>> itsS = stock.asks.entrySet().iterator();

        if (!itsB.hasNext()) {
            return false;
        } else {
            buyLdr = itsB.next().getValue();
        }

        if (!itsS.hasNext()) {
            return false;
        } else {
            sellLdr = itsS.next().getValue();
        }

        delPrcLdrList.clear();
        delPrcLdrList2.clear();

        boolean nextBuyOrd = true;
        boolean nextSellOrd = true;
        BigDecimal qty = BigDecimal.ZERO;

        while (remainQty.compareTo(BigDecimal.ZERO) > 0) {
            // Get an buy order when needed
            while (nextBuyOrd == true) {
                buyOrd = buyLdr.orderList.poll();
                if (buyOrd == null) {
                    if (!itsB.hasNext()) {
                        return false;
                    } else {
                        buyLdr = itsB.next().getValue();
                        continue;
                    }
                } else {
                    if (buyOrd.isDelflg() == true) {
                        continue;
                    } else {
                        break;
                    }
                }
            }

            // Get a sell order when needed
            while (nextSellOrd == true) {
                sellOrd = sellLdr.orderList.poll();
                if (sellOrd == null) {
                    if (!itsS.hasNext()) {
                        return false;
                    } else {
                        sellLdr = itsS.next().getValue();
                        continue;
                    }
                } else {
                    if (sellOrd.isDelflg() == true) {
                        continue;
                    } else {
                        break;
                    }
                }
            }

            // match their quantity
            qty = min(buyOrd.getOver(), sellOrd.getOver());
            qty = min(qty, remainQty);

            buyOrd.setOver(buyOrd.getOver().subtract(qty));
            buyLdr.over = buyLdr.over.subtract(qty);

            if (buyLdr.over.compareTo(BigDecimal.ZERO) <= 0) {
                delPrcLdrList.add(buyLdr.score);
            }

            sellOrd.setOver(sellOrd.getOver().subtract(qty));
            sellLdr.over = sellLdr.over.subtract(qty);

            if (sellLdr.over.compareTo(BigDecimal.ZERO) <= 0) {
                delPrcLdrList2.add(sellLdr.score);
            }

            remainQty = remainQty.subtract(qty);

            if (this.evtCbs != null) {
                evtCbs.callAuctionMatch(buyOrd, sellOrd, qty, result.price);

            }

            if (buyOrd.getOver().compareTo(BigDecimal.ZERO) <= 0) {
                if (this.evtCbs != null) {
                    evtCbs.leaveOrderBook(buyOrd);
                }

                nextBuyOrd = true;
            } else {
                nextBuyOrd = false;
            }

            if (sellOrd.getOver().compareTo(BigDecimal.ZERO) <= 0) {
                if (this.evtCbs != null) {
                    evtCbs.leaveOrderBook(sellOrd);
                }

                nextSellOrd = true;
            } else {
                nextSellOrd = false;
            }
        }

        // Delete and recycle PRICE LEADERS
        Iterator<BigDecimal> myIter = delPrcLdrList.iterator();
        while (myIter.hasNext()) {
            PriceLeader rmvLdr = stock.bids.remove(myIter.next());
            prcLdrPool.putObj(rmvLdr);
        }

        myIter = delPrcLdrList2.iterator();
        while (myIter.hasNext()) {
            PriceLeader rmvLdr = stock.asks.remove(myIter.next());
            prcLdrPool.putObj(rmvLdr);
        }

        return true;
    }


}
