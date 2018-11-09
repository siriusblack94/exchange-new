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
 * 当钱包需要归集的时候,会吧数据插入到该表,现在一般是用在eth和eth这类型需要归集的币种
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("wallet_collect_task")
public class WalletCollectTask extends Model<WalletCollectTask> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 币种ID
     */
    @TableField("coin_id")
    private Long coinId;

    /**
     * 任务的币种名称
     */
    @TableField("coin_type")
    private String coinType;

    /**
     * 任务的币种名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 来自哪个用户
     */
    @TableField("user_id")
    private Long userId;

    /**
     * txid
     */
    private String txid;
    /**
     * 归集数量
     */
    private BigDecimal amount;
    /**
     * 归集手续费
     */
    private BigDecimal chainFee;
    /**
     * 备注
     */
    private String mark;
    /**
     * 是否处理
     */
    private Integer status;

    /**
     * 来自哪个地址
     */
    @TableField("from_address")
    private String fromAddress;

    /**
     * 转到哪里
     */
    @TableField("to_address")
    private String toAddress;

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
