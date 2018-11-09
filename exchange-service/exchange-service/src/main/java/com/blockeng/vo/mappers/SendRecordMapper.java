package com.blockeng.vo.mappers;

import com.blockeng.dto.Sms;
import com.blockeng.repository.SendRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author qiang
 */
@Mapper
public interface SendRecordMapper {

    SendRecordMapper INSTANCE = Mappers.getMapper(SendRecordMapper.class);

    SendRecord map(Sms sms);
}
