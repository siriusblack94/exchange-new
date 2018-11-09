package com.blockeng.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blockeng.dto.NoticeDTO;
import com.blockeng.entity.Notice;

/**
 * <p>
 * 系统资讯公告信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 分页查询公告信息
     *
     * @param page
     * @return
     */
    IPage<NoticeDTO> queryNoticeList(IPage<Notice> page);
}
