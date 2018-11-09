package com.blockeng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blockeng.dto.NoticeDTO;
import com.blockeng.dto.NoticeDTOMapper;
import com.blockeng.entity.Notice;
import com.blockeng.framework.enums.BaseStatus;
import com.blockeng.mapper.NoticeMapper;
import com.blockeng.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 系统资讯公告信息 服务实现类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    /**
     * 分页查询公告信息
     *
     * @param page
     * @return
     */
    @Override
    public IPage<NoticeDTO> queryNoticeList(IPage<Notice> page) {
        QueryWrapper<Notice> wrapper = new QueryWrapper<>();
        wrapper.eq("status", BaseStatus.EFFECTIVE.getCode())
                .orderByDesc("created");
        IPage<Notice> noticePage = super.selectPage(page, wrapper);
        List<Notice> records = noticePage.getRecords();
        List<NoticeDTO> notices = NoticeDTOMapper.INSTANCE.from(records);
        IPage<NoticeDTO> noticeList = new Page<>();
        noticeList.setRecords(notices)
                .setTotal(noticePage.getTotal())
                .setSize(noticePage.getSize())
                .setCurrent(noticePage.getCurrent());
        return noticeList;
    }
}
