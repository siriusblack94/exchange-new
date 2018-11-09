package com.blockeng.vo.mappers;

import com.blockeng.dto.RandCode;
import com.blockeng.vo.RandCodeForm;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface RandCodeMapper {

    RandCodeMapper INSTANCE = Mappers.getMapper(RandCodeMapper.class);

    RandCode map(RandCodeForm form);
}
