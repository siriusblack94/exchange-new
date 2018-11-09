package com.blockeng.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blockeng.dto.NoticeDTO;
import com.blockeng.entity.Notice;
import com.blockeng.framework.http.Response;
import com.blockeng.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/15 下午2:31
 * @Modified by: Chen Long
 */
@RestController
@RequestMapping("/notice")
@Slf4j
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 查询公告列表
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/{current}/{size}")
    public Response notice(@PathVariable("current") int current, @PathVariable("size") int size) {
        IPage<Notice> page = new Page<>();
        page.setCurrent(current).setSize(size);
        IPage<NoticeDTO> noticeList = noticeService.queryNoticeList(page);
        return Response.ok(noticeList);
    }

    /**
     * 查询公告详情
     *
     * @return
     */
    @GetMapping("/{noticeId}")
    public Response notice(@PathVariable("noticeId") Long noticeId) {
        Notice notice = noticeService.selectById(noticeId);
        return Response.ok(notice);
    }
}
