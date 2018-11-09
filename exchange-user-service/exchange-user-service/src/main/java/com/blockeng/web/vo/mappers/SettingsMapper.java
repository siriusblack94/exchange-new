package com.blockeng.web.vo.mappers;

import com.blockeng.entity.User;
import com.blockeng.web.vo.SettingsForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface SettingsMapper {

    SettingsMapper INSTANCE = Mappers.getMapper(SettingsMapper.class);

    User map(SettingsForm form);
}