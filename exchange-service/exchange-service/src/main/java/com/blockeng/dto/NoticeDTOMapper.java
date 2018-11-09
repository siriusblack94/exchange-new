package com.blockeng.dto;

import com.blockeng.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qiang
 */
@Mapper
public interface NoticeDTOMapper {

    NoticeDTOMapper INSTANCE = Mappers.getMapper(NoticeDTOMapper.class);

    NoticeDTO from(Notice notice);

    List<NoticeDTO> from(List<Notice> noticeList);
}