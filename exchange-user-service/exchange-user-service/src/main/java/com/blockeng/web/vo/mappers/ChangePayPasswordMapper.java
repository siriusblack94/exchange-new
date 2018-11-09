package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.ChangePasswordForm;
import com.blockeng.web.vo.ChangePayPasswordForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface ChangePayPasswordMapper {

    ChangePayPasswordMapper INSTANCE = Mappers.getMapper(ChangePayPasswordMapper.class);

    User map(ChangePayPasswordForm form);
}