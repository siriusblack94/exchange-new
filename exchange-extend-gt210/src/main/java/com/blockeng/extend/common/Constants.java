package com.blockeng.extend.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数，变量文件
 * 
 * @author Administrator
 * 
 */
public final class Constants {
	// 0 eth 1 usdt 2 btc 3 gtb
	private static final Map<String,String> numberKeyMap = new HashMap<>();
	private static final Map<String,String> nameKeyMap = new HashMap<>();
	public static Map<String,String> getNumberKeyMap(){
		numberKeyMap.put("0","ETH");
		numberKeyMap.put("1","USDT");
		numberKeyMap.put("2","BTC");
		numberKeyMap.put("3","GTB");
		return numberKeyMap;
	}
	public static Map<String,String> getNameKeyMap(){
		nameKeyMap.put("ETH","0");
		nameKeyMap.put("USDT","1");
		nameKeyMap.put("BTC","2");
		nameKeyMap.put("GTB","3");
		return nameKeyMap;
	}

}
