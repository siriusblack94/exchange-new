package com.blockeng.dto;

import com.blockeng.entity.User;
import com.blockeng.entity.UserLoginLog;
import com.blockeng.framework.security.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface UserDetailsMapper {

    UserDetailsMapper INSTANCE = Mappers.getMapper(UserDetailsMapper.class);

    UserDetails toUserDetails(User user);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "userId", source = "id"),
    })
    UserLoginLog toUserLoginLog(UserDetails userDetails);
}