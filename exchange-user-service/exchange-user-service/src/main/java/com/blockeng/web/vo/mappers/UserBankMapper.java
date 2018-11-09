package com.blockeng.web.vo.mappers;

import com.blockeng.entity.UserBank;
import com.blockeng.web.vo.UserBankForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserBankMapper {

    UserBankMapper INSTANCE = Mappers.getMapper(UserBankMapper.class);

    UserBank map(UserBankForm form);
}