package com.blockeng.dto;

import com.blockeng.entity.EntrustOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface EntrustOrderDTOMapper {

    EntrustOrderDTOMapper INSTANCE = Mappers.getMapper(EntrustOrderDTOMapper.class);

    EntrustOrderDTO from(EntrustOrder entrustOrder);

    EntrustOrderMatchDTO toEntrustMatch(EntrustOrder entrustOrder);
}
