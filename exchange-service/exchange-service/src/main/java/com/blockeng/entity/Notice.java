package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统资讯公告信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 文章状态
     */
    private Integer status;
    /**
     * 文章排序，越大越靠前
     */
    private Integer sort;
    /**
     * 内容
     */
    private String content;
    /**
     * 最后修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建日期
     */
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
