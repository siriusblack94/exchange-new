package com.blockeng.admin.service;


import com.blockeng.admin.dto.AssetAnalysisDTO;

import java.math.BigDecimal;
import java.util.Map;


/**
 * @Author: jakiro
 * @Date: 2018-10-14 11:22
 * @Description: 资产分析分析接口
 */
public interface AssetAnalysisService {

    /**
     * 通过用户ID 获取平台有效币种各种资产值(非挖矿)
     * */
    AssetAnalysisDTO assetAnalysisWithoutMining(String userId);

    /**
     * 通过用户ID 获取平台有效币种各种资产值(挖矿)
     * */
    AssetAnalysisDTO assetAnalysisWithMining(String userId);

}
