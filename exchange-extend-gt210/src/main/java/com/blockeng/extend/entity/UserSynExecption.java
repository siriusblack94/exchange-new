package com.blockeng.extend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户同步异常
 */
@Data
@Accessors(chain = true)
@TableName("user_syn_exception")
public class UserSynExecption {

    /**
     * 自增id
     */
    @TableId(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    /**
     * 用户信息
     */
    @TableField("user_info")
    private String userInfo;
    /**
     * 同步类型
     */
    @TableField("type")
    private String type;
    /**
     * 创建时间
     */
    @TableField("created")
    private String created;


}
