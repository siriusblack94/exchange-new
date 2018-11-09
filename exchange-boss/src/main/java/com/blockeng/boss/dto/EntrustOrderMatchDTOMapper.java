package com.blockeng.boss.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface EntrustOrderMatchDTOMapper {

    EntrustOrderMatchDTOMapper INSTANCE = Mappers.getMapper(EntrustOrderMatchDTOMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "buyOrderId"),
            @Mapping(target = "userId", source = "buyUserId"),
            @Mapping(target = "marketId", source = "marketId"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "marketName", source = "marketName"),
            @Mapping(target = "marketType", source = "marketType"),
            @Mapping(target = "coinId", source = "buyCoinId"),
            @Mapping(target = "price", source = "buyPrice"),
            @Mapping(target = "feeRate", source = "buyFeeRate"),
            @Mapping(target = "volume", source = "buyVolume"),
            @Mapping(target = "deal", defaultValue = "0"),
            @Mapping(target = "type", source = "tradeType"),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "status", defaultValue = "0"),

    })
    EntrustOrderMatchDTO fromBuyOrder(DealOrder dealOrder);

    @Mappings({
            @Mapping(target = "id", source = "sellOrderId"),
            @Mapping(target = "userId", source = "sellUserId"),
            @Mapping(target = "marketId", source = "marketId"),
            @Mapping(target = "symbol", source = "symbol"),
            @Mapping(target = "marketName", source = "marketName"),
            @Mapping(target = "marketType", source = "marketType"),
            @Mapping(target = "coinId", source = "sellCoinId"),
            @Mapping(target = "price", source = "sellPrice"),
            @Mapping(target = "feeRate", source = "sellFeeRate"),
            @Mapping(target = "volume", source = "sellVolume"),
            @Mapping(target = "deal", defaultValue = "0"),
            @Mapping(target = "type", source = "tradeType"),
            @Mapping(target = "created", ignore = true),
            @Mapping(target = "status", defaultValue = "0"),

    })
    EntrustOrderMatchDTO fromSellOrder(DealOrder dealOrder);
}
