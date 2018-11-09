package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.ChangePhoneForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface ChangePhoneMapper {

    ChangePhoneMapper INSTANCE = Mappers.getMapper(ChangePhoneMapper.class);

    User map(ChangePhoneForm form);
}