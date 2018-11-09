package com.blockeng.dto;

import com.blockeng.entity.UserBank;
import com.blockeng.user.dto.UserBankDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface UserBankDTOMapper {

    UserBankDTOMapper INSTANCE = Mappers.getMapper(UserBankDTOMapper.class);

    UserBankDTO from(UserBank userBank);
}