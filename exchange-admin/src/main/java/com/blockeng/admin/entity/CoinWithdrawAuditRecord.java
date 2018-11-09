package com.blockeng.admin.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.Version;

import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 提币审核记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("coin_withdraw_audit_record")
public class CoinWithdrawAuditRecord extends Model<CoinWithdrawAuditRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 提币订单号
     */
    @TableField("order_id")
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
