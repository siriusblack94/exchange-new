package com.blockeng.wallet.entity;

import com.clg.wallet.bean.ClientBean;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientBeanMapper {

    ClientBeanMapper INSTANCE = Mappers.getMapper(ClientBeanMapper.class);

    ClientBean form(CoinConfig coinConfig);
}
