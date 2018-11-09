package com.blockeng.service;

import com.blockeng.dto.CoinTransferDTO;
import com.blockeng.entity.CoinTransfer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: jakiro
 * @Date: 2018-10-30 14:17
 * @Description:
 */
public interface CoinTransferService {


    public boolean transferAccounts(Long moneyMakerUserId, Long coinId, BigDecimal num,Long payeeUserId);

    public List<CoinTransferDTO> getCoinTransferDetail(int current, int size, String startTime, String endTime,Long coinId,Long userId, int transferMethod);
}
