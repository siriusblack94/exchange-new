package com.blockeng.admin.dto;

import com.blockeng.admin.entity.EntrustOrder;
import com.blockeng.dto.EntrustOrderMatchDTO;
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
