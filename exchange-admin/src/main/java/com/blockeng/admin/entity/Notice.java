package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统资讯公告信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "文章实体")
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title", required = true)
    private String title;
    /**
     * 简介
     */
    @ApiModelProperty(value = "简介", name = "description", required = false)
    private String description;
    /**
     * 作者
     */
    @ApiModelProperty(value = "作者", name = "author", required = false)
    private String author;
    /**
     * 文章状态
     */
    @ApiModelProperty(value = "文章状态", name = "status", required = false)
    private Integer status;
    /**
     * 文章排序，越大越靠前
     */
    @ApiModelProperty(value = "文章排序，越大越靠前", name = "sort", required = true)
    private Integer sort;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", name = "content", required = false)
    private String content;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间", name = "lastUpdateTime", required = false)
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建日期
     */
    @ApiModelProperty(value = " 创建日期", name = "created", required = false)
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
