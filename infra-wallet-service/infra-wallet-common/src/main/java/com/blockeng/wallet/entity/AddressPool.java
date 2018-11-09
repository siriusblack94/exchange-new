package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户的地址池
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("address_pool")
public class AddressPool extends Model<AddressPool> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
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
     * 地址类型
     */
    private String coinType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
