package org.jopenexchg;

public class MatcherDemo {
    static final int ORDER_MAX = 5000 * 1000;
    static final int ORDER_CNT = 3000 * 1000;
    static final int PRCLD_CNT = 1000;
    static final int STOCK_CNT = 10;

    /**
     * 对本测试的说明
     * 考虑到未来网络收单线程应该是按全序收单
     * 收下的全部指令在完成格式化后建议放入一
     * 预先开好的大的连续数组
     * <p>
     * 预处理线程应该自前向后扫描这个数组，若有
     * 新增则处理之并写上批复意见
     * <p>
     * 后续的撮合线程应该做一部分预处理工作，主
     * 要应该是持仓相关检查——因为如果预处理线程
     * 也做，就需要引入锁，就不划算了；撮合线程
     * 当然也应该同时做撮合
     * <p>
     * 由于网络接收线程、预处理线程和撮合线程是并
     * 行执行的，所以本测试测试的撮合线程的性能，
     * 为此扣除了在输入队列中填写数据的时间
     */
    static void speedTest_01() {
/*        try {
            Matcher matcher = MatchingEngine.matcher;
            Order order = matcher.allocOrder();
            order.symbol = "BTCUSDT";
            order.type = OrderType.SELL;
            order.vol = BigDecimal.ONE;
            order.price = BigDecimal.ONE;

            for (int i = 0; i < 10; i++) {
                Order order1 = matcher.allocOrder();
                order1.symbol = "BTCUSDT";
                order1.type = OrderType.BUY;
                order1.vol = BigDecimal.TEN;
                order1.price = BigDecimal.TEN;
                matcher.matchInsOrder(order1);
            }

            if (false == matcher.matchInsOrder(order)) {
                System.out.println("Error occured");
            }
            System.out.println(matcher.delOrder(order));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
	
/*	static void funcTest_02()
	{
		try
		{
			Matcher matcher = new Matcher(200, 1000);
			EventHandlerDebugImpl evtCbs = new EventHandlerDebugImpl();
			
			matcher.setEvtCbs(evtCbs);
			
			int stockId = 600688;
			String stockName = "_" + stockId + "_";
			TradedInst stock = matcher.addStock(stockId, stockName);

			Order ordr = null;
			
			// Buy eating Sell
			// Sell Side 
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 10;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 40;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);			
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 100;
			ordr.ordPrc = 1100;
			matcher.ocallInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 30;
			ordr.ordPrc = 1000;
			matcher.ocallInsOrder(ordr);	
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 70;
			ordr.ordPrc = 1000;
			matcher.ocallInsOrder(ordr);	
			
			// Buy Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 25;
			ordr.ordPrc = 1300;
			matcher.ocallInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 25;
			ordr.ordPrc = 1300;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 30;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 20;
			ordr.ordPrc = 1200;
			matcher.ocallInsOrder(ordr);			
			
			CallAuctionResult result = new CallAuctionResult();
			boolean bOK = matcher.calcCallAuction(stock, result);
			
			System.out.println("**************");
			bOK = matcher.doCallAuction(stock, result);
			
			System.out.println("");
			System.out.println("doOCall bOK = " + bOK + " ; price = " + result.price + " ; qty = " + result.volume);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
		

	static void funcTest_01()
	{
		try
		{
			Matcher matcher = new Matcher(200, 1000);
			
			EventHandlerDebugImpl evtCb = new EventHandlerDebugImpl();
			matcher.setEvtCbs(evtCb);
			
			int stockId = 600688;
			String stockName = "_" + stockId + "_";
			matcher.addStock(stockId, stockName);

			stockId = 600666;
			stockName = "_" + stockId + "_";
			matcher.addStock(stockId, stockName);
			
			Order ordr = null;
			
			// Buy eating Sell
			// Sell Side 
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 100;
			ordr.ordPrc = 512;
			matcher.matchInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 200;
			ordr.ordPrc = 512;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 150;
			ordr.ordPrc = 515;
			matcher.matchInsOrder(ordr);	

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 305;
			ordr.ordPrc = 515;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = false;
			ordr.ordQty = 400;
			ordr.ordPrc = 518;
			matcher.matchInsOrder(ordr);
			
			// Incoming Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600688;
			ordr.isbuy = true;
			ordr.ordQty = 1000;
			ordr.ordPrc = 518;
			matcher.matchInsOrder(ordr);
			
			// Sell eating Buy
			// Buy Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 100;
			ordr.ordPrc = 1050;
			matcher.matchInsOrder(ordr);
			
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 200;
			ordr.ordPrc = 1050;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 150;
			ordr.ordPrc = 1047;
			matcher.matchInsOrder(ordr);	

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 305;
			ordr.ordPrc = 1047;
			matcher.matchInsOrder(ordr);

			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = true;
			ordr.ordQty = 400;
			ordr.ordPrc = 1045;
			matcher.matchInsOrder(ordr);
			
			// Incoming Side
			ordr = matcher.allocOrder();			
			ordr.stockid = 600666;
			ordr.isbuy = false;
			ordr.ordQty = 1000;
			ordr.ordPrc = 1044;
			matcher.matchInsOrder(ordr);			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
			
	static void randTest()
	{
		int i = 0;
		Order ordr = new Order();

		// generate enough orders
		long start = System.currentTimeMillis();
		
		for(i = 0; i < ORDER_CNT; i++)
		{
			fakeOrder(ordr);
		}
		long end = System.currentTimeMillis();
		
		System.out.println(ORDER_CNT + " orders faked in " + (end - start + 1) + " ms");
		System.out.println("Speed is " + ORDER_CNT / ((double)(end - start + 1) / 1000.0) + " orders/s");
	}*/

    /**
     * @param args
     */
    public static void main(String[] args) {
        //funcTest_01();
        speedTest_01();
        //funcTest_02();
        // randTest();   // 610ms
    }

}
