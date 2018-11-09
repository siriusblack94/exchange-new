package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.RegisterForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface RegisterMapper {

    RegisterMapper INSTANCE = Mappers.getMapper(RegisterMapper.class);

    User map(RegisterForm form);
}