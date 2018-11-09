package com.blockeng.api.service;

import com.blockeng.api.dto.AICoinExchangeInfoDTO;
import com.blockeng.api.dto.AICoinTradeDTO;
import com.blockeng.api.dto.DepthsVO;

import java.util.List;
import java.util.Map;

public interface AICoinService {

    public Map<String,Object> tickers();

    public DepthsVO depth(String symbol, int size);

    public List<AICoinTradeDTO> trades(String symbol, int size);

    public List<Object> klineOneMin(String symbol, String type, int size);

    public List<AICoinExchangeInfoDTO> exchangeInfo();

}
