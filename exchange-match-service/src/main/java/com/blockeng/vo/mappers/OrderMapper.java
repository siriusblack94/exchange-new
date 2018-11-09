package com.blockeng.vo.mappers;

import com.blockeng.entity.EntrustOrder;
import com.blockeng.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order form(EntrustOrder entrustOrder);
}
