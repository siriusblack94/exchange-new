package com.blockeng.dto;

import com.blockeng.entity.EntrustOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/19 上午11:48
 * @Modified by: Chen Long
 */
@Mapper
public interface TradeEntrustOrderDTOMapper {

    TradeEntrustOrderDTOMapper INSTANCE = Mappers.getMapper(TradeEntrustOrderDTOMapper.class);

    TradeEntrustOrderDTO from(EntrustOrder entrustOrder);

    List<TradeEntrustOrderDTO> from(List<EntrustOrder> entrustOrderList);
}
