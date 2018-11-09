package com.blockeng.dto;

import com.blockeng.entity.Market;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/15 下午5:40
 * @Modified by: Chen Long
 */
@Mapper
public interface MarketDTOMapper {

    MarketDTOMapper INSTANCE = Mappers.getMapper(MarketDTOMapper.class);

    MarketDTO from(Market market);

    List<MarketDTO> from(List<Market> marketList);
}
