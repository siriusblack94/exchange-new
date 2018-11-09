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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 平台归账手续费等账户
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("admin_address")
public class AdminAddress extends Model<AdminAddress> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * eth keystore
     */
    @ApiModelProperty(value = "密钥", name = "keystore", example = "", required = true)
    private String keystore;
    /**
     * eth账号密码
     */
    @ApiModelProperty(value = "密码", name = "pwd", example = "", required = true)
    private String pwd;
    /**
     * 币种Id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 地址
     */
    @ApiModelProperty(value = "钱包地址", name = "address", example = "", required = true)
    private String address;
    /**
     * 1:归账,2:打款,3:手续费,4:充值地址
     */
    @ApiModelProperty(value = "地址类型", name = "status", example = "1 归账,2 打款,3 手续费 4:充值地址", required = true)
    private Integer status;
    /**
     * 类型
     */
    @TableField("coin_type")
    @ApiModelProperty(value = "币种类型", name = "coinType", example = "xnb 人民币,default 比特币系列,ETH 以太坊,ethToken以太坊代币")
    private String coinType;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
