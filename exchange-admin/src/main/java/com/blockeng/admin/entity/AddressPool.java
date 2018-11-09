package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户的地址池
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("address_pool")
public class AddressPool extends Model<AddressPool> {

    private static final long serialVersionUID = 1L;
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 币种ID
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 地址
     */
    private String address;

    /**
     * keystore
     */
    private String keystore;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 地址类型
     */
    @TableField("coin_type")
    private String coinType;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
