package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: 公告
 * @Author: Chen Long
 * @Date: Created in 2018/6/15 下午2:36
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class NoticeDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 创建日期
     */
    private Date created;
}
