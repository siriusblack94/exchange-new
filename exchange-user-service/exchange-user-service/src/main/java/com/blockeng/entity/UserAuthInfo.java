package com.blockeng.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 实名认证信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("user_auth_info")
public class UserAuthInfo extends Model<UserAuthInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 图片地址
     */
    @TableField("image_url")
    private String imageUrl;
    /**
     * 序号：1-身份证正面照；2-身份证反面照；3-手持身份证照片；
     */
    private Integer serialno;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;

    @TableField("auth_code")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authCode;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
