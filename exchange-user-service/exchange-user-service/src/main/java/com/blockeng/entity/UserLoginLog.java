package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author maple
 * @date 2018/10/25 16:30
 **/
@TableName("user_login_log")
@Accessors(chain = true)
@Data
public class UserLoginLog extends Model<UserLoginLog> {

    private static final long serialVersionUID = 257L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField("client_type")
    private Integer clientType;

    @TableField("login_ip")
    private String loginIp;

    @TableField("login_address")
    private String loginAddress;

    @TableField("login_time")
    private Date loginTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
