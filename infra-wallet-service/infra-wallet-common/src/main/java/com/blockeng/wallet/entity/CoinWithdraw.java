package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 当用户发起提币的时候,吧数据插入到该表
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("coin_withdraw")
public class CoinWithdraw extends Model<CoinWithdraw> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

//    @TableField("apply_id")
//    private Long applyId;

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
     * 0：审核中；1：已转出；2：拒绝；3：撤销；4,5：打币中 5(代表钱包打币成功)；
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
     * 0站内1其他
     */
    private BigDecimal chainFee;

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
     * 备注
     */
    private String walletMark;

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
