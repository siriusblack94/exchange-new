package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 网站配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("web_config")
public class WebConfig extends Model<WebConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * Id
     */
    @ApiModelProperty(value = "分组", name = "id", required = false, dataType = "Long")
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 分组
     */
    @ApiModelProperty(value = "分组", name = "type", required = true)
    private String type;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称", name = "name", required = false)
    private String name;
    /**
     * 值
     */
    @ApiModelProperty(value = "值", name = "value", required = true)
    private String value;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort", required = false)
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)
    private Date created;
    /**
     * 网页文本
     */
    @ApiModelProperty(value = "网页文本", name = "url", required = false)
    private String url;

    /***
     *是否使用 0 否 1是
     */
    @ApiModelProperty(value = "是否使用 0 否 1是", name = "status", required = true)
    @TableField("status")
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
