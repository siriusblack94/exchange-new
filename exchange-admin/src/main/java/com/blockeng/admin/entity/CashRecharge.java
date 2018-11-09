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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 充值表
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("cash_recharge")
@ApiModel(description = "充值表")
public class CashRecharge extends Model<CashRecharge> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 币种id
     */
    @TableField("coin_id")
    @ApiModelProperty(value = "币种id", name = "coinId", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 币种名：cny，人民币；
     */
    @TableField("coin_name")
    @ApiModelProperty(value = "币种名：cny，人民币；", name = "coinName", required = true)

    private String coinName;
    /**
     * 数量（充值金额）
     */
    @ApiModelProperty(value = "数量（充值金额）", name = "num", required = true)
    private BigDecimal num;
    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费", name = "fee", required = true)
    private BigDecimal fee;
    /**
     * 手续费币种
     */
    @ApiModelProperty(value = "手续费币种", name = "feecoin", required = false)
    private String feecoin;
    /**
     * 成交量（到账金额）
     */
    @ApiModelProperty(value = "成交量（到账金额）", name = "mum", required = true)
    private BigDecimal mum;
    /**
     * 类型：alipay，支付宝；cai1pay，财易付；bank，银联；
     */
    @ApiModelProperty(value = "类型：alipay，支付宝；cai1pay，财易付；bank，银联；", name = "type", required = true)

    private String type;
    /**
     * 充值订单号
     */
    @ApiModelProperty(value = "充值订单号", name = "type", required = true)
    private String tradeno;
    /**
     * 第三方订单号
     */
    @ApiModelProperty(value = "第三方订单号", name = "outtradeno", required = false)
    private String outtradeno;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    private String remark;
    /**
     * 审核备注
     */
    @TableField("audit_remark")
    @ApiModelProperty(value = "审核备注", name = "auditRemark", required = false)
    private String auditRemark;
    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数", name = "status", required = true)
    private Integer step;
    /**
     * 状态：0，未付款；1，到账成功；2，人工到账；3，处理中；
     */
    @ApiModelProperty(value = "状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；", name = "status", required = true)
    private Integer status;
    /**
     * 创建时间
     */
    private Date created;
    /**
     * 更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    @ApiModelProperty(value = "最后确认充值到账时间", name = "lastTime", required = true)
    @TableField("last_time")
    private Date lastTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
