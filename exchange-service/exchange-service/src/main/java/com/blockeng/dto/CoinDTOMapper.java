package com.blockeng.dto;

import com.blockeng.entity.Coin;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Description: 币种信息
 * @Author: Chen Long
 * @Date: Created in 2018/5/21 下午7:19
 * @Modified by: Chen Long
 */
@Mapper
public interface CoinDTOMapper {

    CoinDTOMapper INSTANCE = Mappers.getMapper(CoinDTOMapper.class);

    CoinDTO from(Coin coin);

    List<CoinDTO> from(List<Coin> coins);
}
