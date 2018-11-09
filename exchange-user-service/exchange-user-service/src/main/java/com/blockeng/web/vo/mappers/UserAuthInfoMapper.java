package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.UserAuthInfoForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface UserAuthInfoMapper {

    UserAuthInfoMapper INSTANCE = Mappers.getMapper(UserAuthInfoMapper.class);

    User map(UserAuthInfoForm form);
}