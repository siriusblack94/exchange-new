package com.blockeng.web.vo.mappers;

import com.blockeng.entity.UserAuthInfo;
import com.blockeng.web.vo.AuthUserForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface AuthUserMapper {

    AuthUserMapper INSTANCE = Mappers.getMapper(AuthUserMapper.class);

    UserAuthInfo map(AuthUserForm form);
}