package com.blockeng.dto;

import com.blockeng.entity.TurnoverOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/17 下午3:30
 * @Modified by: Chen Long
 */
@Mapper
public interface TxDTOMapper {

    TxDTOMapper INSTANCE = Mappers.getMapper(TxDTOMapper.class);

    TurnoverOrder from(TxDTO txDTO);
}
