package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@ApiModel(description = "平台配置信息")
public class Config extends Model<Config> {

    private static final long serialVersionUID = 1L;
    /**
     * 平台规则id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 配置规则类型
     */
    @ApiModelProperty(value = "配置规则类型64", name = "type", required = true)
    private String type;
    /**
     * 配置规则代码
     */
    @ApiModelProperty(value = "配置规则代码50", name = "code", required = true)
    private String code;
    /**
     * 配置规则名称
     */
    @ApiModelProperty(value = "配置规则名称100", name = "name", required = true)
    private String name;
    /**
     * 配置规则描述
     */
    @ApiModelProperty(value = "配置规则描述255", name = "desc", required = false)
    private String desc;
    /**
     * 配置值
     */
    @ApiModelProperty(value = "配置值255", name = "value", required = true)
    private String value;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)
    private Date created;

    /*
      看似多余字段，这里没有这个字段会解析错误，报400 2018.8.30
     */
    @ApiModelProperty(value = "规则描述", name = "created", required = false)
    private String description;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
