package com.blockeng.dto;

import com.blockeng.entity.EntrustOrder;
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
