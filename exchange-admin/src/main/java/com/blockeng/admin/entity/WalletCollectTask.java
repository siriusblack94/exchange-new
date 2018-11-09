package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;

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
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("wallet_collect_task")
public class WalletCollectTask extends Model<WalletCollectTask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 币种ID
     */
    @TableField("coin_id")
    private Integer coinId;
    /**
     * 来自哪个地址
     */
    @TableField("from_address")
    private String fromAddress;
    /**
     * 币种类型
     */
    @TableField("coin_type")
    @ApiModelProperty(value = "币种类型", name = "coinType", example = "xnb 人民币,default 比特币系列,ETH 以太坊,ethToken以太坊代币")
    private String coinType;
    /**
     * 来自哪个用户
     */
    @TableField("user_id")
    private Integer userId;
    /**
     * txid
     */
    private String txid;
    /**
     * 归集数量
     */
    private BigDecimal amount;
    /**
     * 备注
     */
    private String mark;
    /**
     * 是否处理
     */
    private Integer status;
    /**
     * 转到哪里
     */
    @TableField("to_address")
    private String toAddress;
    /**
     * 币种名称
     */
    @TableField("coin_name")
    private String coinName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
