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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 数字货币提现记录
 * </p>
 *
 * @author crow
 * @since 2018-05-16
 */
@Data
@Accessors(chain = true)
@TableName("coin_withdraw")
public class CoinWithdraw extends Model<CoinWithdraw> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
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
     * 当前审核级数
     */
    private Integer step;

    /**
     * 状态：0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中；
     */
    private Integer status;

    /**
     * 交易id
     */
    private String txid;

    /**
     * 提现量
     */
    private BigDecimal num;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 实际提现
     */
    private BigDecimal mum;

    /**
     * 0站内1其他
     */
    private Integer type;

    /**
     * 区块高度
     */
    @TableField("block_num")
    private Integer blockNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 钱包提币备注
     */
    @TableField("wallet_mark")
    private String walletMark;

    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;

    /**
     * 创建时间
     */
    private Date created;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
