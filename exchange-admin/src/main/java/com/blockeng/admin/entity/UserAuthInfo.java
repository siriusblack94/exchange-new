package com.blockeng.admin.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 实名认证信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("user_auth_info")
public class UserAuthInfo extends Model<UserAuthInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "id", name = "id", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户ID", name = "userId", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 一组图片的唯一code
     */
    @TableField("auth_code")
    @ApiModelProperty(value = "图片code", name = "authCode", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authCode;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址", name = "imageUrl", required = false)

    @TableField("image_url")
    private String imageUrl;
    /**
     * 序号：1-身份证正面照；2-身份证反面照；3-手持身份证照片；
     */
    @ApiModelProperty(value = "序号：1-身份证正面照；2-身份证反面照；3-手持身份证照片；", name = "serialno", required = false)

    private Integer serialno;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", required = false)

    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
