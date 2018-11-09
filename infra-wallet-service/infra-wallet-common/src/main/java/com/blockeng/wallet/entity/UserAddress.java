package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户钱包地址信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("user_address")
public class UserAddress extends Model<UserAddress> {

    private static final long serialVersionUID = 1L;

    public UserAddress() {
    }

    public UserAddress(Long userId, Long coinId, String address, String keystore, String pwd) {
        this.userId = userId;
        this.coinId = coinId;
        this.address = address;
        this.keystore = keystore;
        this.pwd = pwd;
    }

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 币种ID
     */
    @TableField("coin_id")
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
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
