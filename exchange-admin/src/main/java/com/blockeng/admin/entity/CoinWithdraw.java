package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.annotations.*;
import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

/**
 * <p>
 * 虚拟币提现
 * </p>
 *
 * @author qiang
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("coin_withdraw")
@ApiModel(value = "CoinWithdraw", description = "虚拟币提现实体")
public class CoinWithdraw extends Model<CoinWithdraw> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 币种名称
     */
    @TableField("coin_name")
    @ApiModelProperty(value = "币种名称", name = "coinName", example = "", required = false)
    private String coinName;

    /**
     * 钱包类型
     */
    @TableField("coin_type")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String coinType;

    /**
     * 钱包地址
     */
    @ApiModelProperty(value = "钱包地址", name = "address", example = "", required = false)
    private String address;

    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数", name = "step")
    private Integer step;

    /**
     * 状态：0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中；
     */
    @ApiModelProperty(value = "状态", name = "status", example = "0", required = true)
    private Integer status;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id", name = "txid", example = "", required = false)
    private String txid;
    /**
     * 提现量
     */
    @ApiModelProperty(value = "提现量", name = "num", example = "", required = false)

    private BigDecimal num;
    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费", name = "fee", example = "", required = false)
    private BigDecimal fee;
    /**
     * 实际提现
     */
    @ApiModelProperty(value = "提现金额", name = "mum", example = "", required = false)
    private BigDecimal mum;
    /**
     * 0站内1站外自动2站外手工提币
     */
    private Integer type;
    /**
     * 链上手续费
     */
    @TableField("chain_fee")
    private BigDecimal chainFee;
    /**
     * 区块高度
     */
    @TableField("block_num")
    @ApiModelProperty(hidden = true)
    private Integer blockNum;

    /**
     * 备注
     */
    @TableField(value = "wallet_mark")
    @ApiModelProperty(value = "钱包提币备注备注", name = "wallet_mark", example = "", required = true)
    private String walletMark;

    /**
     * 备注
     */
    @ApiModelProperty(value = "后台审核人员提币备注备注", name = "remark", example = "", required = true)
    private String remark;

    /**
     * 审核时间
     */
    @TableField("audit_time")
    @ApiModelProperty(value = "审核时间", name = "auditTime", example = "", required = false)
    private Date auditTime;

    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "申请时间", name = "created", example = "", required = false)
    private Date created;

    /**
     * 用户名
     */
    @TableField(value = "userName", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String userName;

    /**
     * 用户手机号
     */
    @TableField(value = "mobile", exist = false)
    @ApiModelProperty(value = "用户手机号", name = "mobile", example = "", required = false)
    private String mobile;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;


    /**
     * 创建时间字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createdStr;

    /**
     * 状态显示字符串：0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中
     * (for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String statusStr;

    public String getIdStr() {
        if (null == this.getId()) {
            return "";
        }
        //导出ID过长+括号处理
        return "[" + this.getId().toString() + "]";
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getStatusStr() {
        StringBuilder str = new StringBuilder();
        if (0 == this.getStatus()) {
            return str.append("审核中").toString();
        }
        if (1 == this.getStatus()) {
            return str.append("转出成功").toString();
        }
        if (2 == this.getStatus()) {
            return str.append("审核拒绝").toString();
        }
        if (3 == this.getStatus()) {
            return str.append("撤销").toString();
        }
        if (4 == this.getStatus()) {
            return str.append("审核通过").toString();
        }
        if (5 == this.getStatus()) {
            return str.append("打币中").toString();
        }
        return "";
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getCreatedStr() {
        if (null == this.getCreated()) {
            return "";
        }
        DateTime dateTime = new DateTime(this.getCreated());
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public void setCreatedStr(String createdStr) {
        this.createdStr = createdStr;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
