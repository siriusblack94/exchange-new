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
 * 平台归账手续费等账户
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("admin_address")
public class AdminAddress extends Model<AdminAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 币种Id
     */
    @TableField("coin_id")
    private Long coinId;
    /**
     * eth keystore
     */
    private String keystore;
    /**
     * eth账号密码
     */
    private String pwd;
    /**
     * 地址
     */
    private String address;
    /**
     * 1:归账,2:打款,3:手续费
     */
    private Integer status;
    /**
     * 类型
     */
    @TableField("coin_type")
    private String coinType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
