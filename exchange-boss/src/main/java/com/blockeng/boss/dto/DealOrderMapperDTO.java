package com.blockeng.boss.dto;

import com.blockeng.boss.entity.TurnoverOrder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/10 上午2:10
 * @Modified by: Chen Long
 */
@Mapper
public interface DealOrderMapperDTO {

    DealOrderMapperDTO INSTANCE = Mappers.getMapper(DealOrderMapperDTO.class);

    TurnoverOrder from(DealOrder dealOrder);
}
