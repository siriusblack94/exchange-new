package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 充值审核记录表表
 * </p>
 *
 * @author lxl
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("cash_recharge_audit_record")
public class CashRechargeAuditRecord extends Model<CashRechargeAuditRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 提币订单号
     */
    @TableField("order_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 审核备注
     */
    private String remark;
    /**
     * 当前审核级数
     */
    private Integer step;
    /**
     * 审核人ID
     */
    @TableField("audit_user_id")
    private Long auditUserId;
    /**
     * 审核人ID
     */
    @TableField("audit_user_name")
    private String auditUserName;
    /**
     * 创建时间
     */
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }


}
