package com.blockeng.dto;

import com.blockeng.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface AccountDTOMapper {

    AccountDTOMapper INSTANCE = Mappers.getMapper(AccountDTOMapper.class);

    AccountDTO from(Account account);
}