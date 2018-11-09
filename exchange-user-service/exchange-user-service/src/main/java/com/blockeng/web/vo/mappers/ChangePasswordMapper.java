package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.ChangePasswordForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface ChangePasswordMapper {

    ChangePasswordMapper INSTANCE = Mappers.getMapper(ChangePasswordMapper.class);

    User map(ChangePasswordForm form);
}