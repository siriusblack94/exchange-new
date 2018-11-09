package com.blockeng.dto;

import com.blockeng.entity.TradeArea;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qiang
 */
@Mapper
public interface TradeAreaDTOMapper {

    TradeAreaDTOMapper INSTANCE = Mappers.getMapper(TradeAreaDTOMapper.class);

    TradeAreaDTO from(TradeArea tradeArea);

    List<TradeAreaDTO> from(List<TradeArea> tradeArea);
}