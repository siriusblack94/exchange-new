package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("coin_buckle_audit_record")
@ApiModel("补扣审核")
public class CoinBuckleAuditRecord extends Model<CoinBuckleAuditRecord> {

    private static final long serialVersionUID = 51L;

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
