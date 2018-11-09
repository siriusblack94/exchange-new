package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.blockeng.dto.TradeAreaDTO;
import com.blockeng.dto.TradeAreaDTOMapper;
import com.blockeng.entity.Market;
import com.blockeng.entity.TradeArea;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.framework.enums.TradeAreaType;
import com.blockeng.mapper.TradeAreaMapper;
import com.blockeng.service.MarketService;
import com.blockeng.service.TradeAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.EntityWriter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 交易区 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class TradeAreaServiceImpl extends ServiceImpl<TradeAreaMapper, TradeArea> implements TradeAreaService {

    @Autowired
    private MarketService marketService;

    /**
     * 查询交易区域(缓存2分钟)
     *
     * @param areaId 交易区域ID
     * @return
     */
    @Override
    public TradeAreaDTO queryTradeAreaFromCache(long areaId) {
        TradeArea tradeArea = baseMapper.selectById(areaId);
        if (tradeArea == null) {
            return null;
        }
        return this.convert(tradeArea);
    }

    /**
     * 根据类型查询交易区域
     *
     * @param tradeAreaType 交易区域
     * @return
     */
    @Override
    public List<TradeAreaDTO> queryByType(TradeAreaType tradeAreaType) {
        List<TradeAreaDTO> tradeAreaDTOS = new ArrayList<>();
        QueryWrapper<TradeArea> query = new QueryWrapper<>();
        query.eq("type", tradeAreaType.getCode())
                .eq("status", BaseStatus.EFFECTIVE.getCode())
                .orderByAsc("sort");
        List<TradeArea> tradeAreas = baseMapper.selectList(query);
        if (!CollectionUtils.isEmpty(tradeAreas)) {
            for (TradeArea tradeArea : tradeAreas) {
                tradeAreaDTOS.add(this.convert(tradeArea));
            }
        }
        return tradeAreaDTOS;
    }

    /**
     * 转换DTO
     *
     * @param tradeArea
     * @return
     */
    private TradeAreaDTO convert(TradeArea tradeArea) {
        TradeAreaDTO tradeAreaDTO = TradeAreaDTOMapper.INSTANCE.from(tradeArea);
        tradeAreaDTO.setMarketIds(this.getMarketIds(tradeArea));
        return tradeAreaDTO;
    }

    /**
     * 交易交易区域下的交易对ID
     *
     * @param tradeArea 交易区域
     * @return
     */
    private String getMarketIds(TradeArea tradeArea) {
        if (tradeArea != null) {
            StringBuffer marketIds = new StringBuffer();
            QueryWrapper<Market> wrapper = new QueryWrapper<>();
            wrapper.eq("trade_area_id", tradeArea.getId())
                    .eq("status", BaseStatus.EFFECTIVE.getCode())
                    .orderByAsc("sort");
            marketService.selectList(wrapper).forEach(market -> {
                marketIds.append(market.getId()).append(",");
            });
            if (marketIds.length() > 0) {
                return marketIds.substring(0, marketIds.length() - 1);
            }
        }
        return "";
    }
}
