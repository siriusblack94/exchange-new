package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.ForgotForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ForgotMapper {

    ForgotMapper INSTANCE = Mappers.getMapper(ForgotMapper.class);

    User map(ForgotForm form);
}