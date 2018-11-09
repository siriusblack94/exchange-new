package com.blockeng.api.service;

import com.blockeng.api.dto.DepthsVO;

import java.math.BigDecimal;

public interface EntrustOrderService  {

        DepthsVO queryDepths(String symbol, BigDecimal mod, int size);

}
