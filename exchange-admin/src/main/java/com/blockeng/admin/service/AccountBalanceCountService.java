package com.blockeng.admin.service;


import com.blockeng.admin.dto.AccountBalanceStatiscDTO;

import java.util.List;
import java.util.Map;

/**
 * 资产统计接口
 * */
public interface AccountBalanceCountService {


     AccountBalanceStatiscDTO accountBalance(String coinId, Integer accountType);


     List<Map<String,Object>> selectBalanceByUser(String userId);
}
