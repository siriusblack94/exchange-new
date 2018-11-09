package com.blockeng.trade.dto;

import com.blockeng.trade.entity.EntrustOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface EntrustOrderMatchDTOMapper {

    EntrustOrderMatchDTOMapper INSTANCE = Mappers.getMapper(EntrustOrderMatchDTOMapper.class);

    EntrustOrderMatchDTO from(EntrustOrder entrustOrder);
}
