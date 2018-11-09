package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("coin_buckle")
@ApiModel(description = "补扣")
public class CoinBuckle extends Model<CoinBuckle> {

    private static final long serialVersionUID = 13L;
    /**
     * 自增id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    private Long id;
    @TableField(exist = false)
    private Long userId;
    @TableField(exist = false)
    private Long coinId;
    @ApiModelProperty(value = "数量（口补金额）", name = "amount", required = true)
    private BigDecimal amount;

    @TableField("account_id")
    private Long accountId;
    /**
     * 扣补类型 1补 2扣
     */
    @ApiModelProperty(value = "类型：1：补币，2：扣币", name = "type", required = true)
    private Integer type;
    @ApiModelProperty(value = "备注", name = "remark", required = false)
    private String remark;
    @ApiModelProperty(value = "摘要", name = "description",required = true)
    private String description;
    @ApiModelProperty(value = "原因", name = "description",required = true)
    private String reason;
    @ApiModelProperty(value = "当前审核级数", name = "status", required = true)
    private Integer step = 1;
    @ApiModelProperty(value = "状态：0-待审核；1-审核通过；2-拒绝；3-充值成功；", name = "status", required = true)
    private Integer status = 0;
    /** 创建时间  */
    private Date created;
    /** 更新时间*/
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /** 审核时间 */
    @TableField("audit_time")
    private Date auditTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
