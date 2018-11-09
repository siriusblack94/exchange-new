package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
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
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("coin_recharge")
public class CoinRecharge extends Model<CoinRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 币种id
     */
    @TableField("coin_id")
    private Long coinId;
    /**
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 币种类型
     */
    @TableField("coin_type")
    private String coinType;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 0正常-1充值失败
     */
    private Integer confirm;

    /**
     * 0正常-1充值失败
     */
    private Integer status;

    /**
     * 交易id
     */
    private String txid;


    /**
     * 充值金额
     */
    private BigDecimal amount;

    /**
     * 修改时间
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
