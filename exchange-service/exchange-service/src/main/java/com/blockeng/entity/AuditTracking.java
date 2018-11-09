package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 审核轨迹表
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("audit_tracking")
public class AuditTracking extends Model<AuditTracking> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 关联Id
     */
    @TableField("union_id")
    private String unionId;
    /**
     * 审核人Id
     */
    @TableField("auditor_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long auditorId;
    /**
     * 审核人姓名
     */
    @TableField("auditor_name")
    private String auditorName;
    /**
     * 审核类型（1提现审核，2提币审核，3实名认证审核，4充值审核）
     */
    private String type;
    /**
     * 当前步骤
     */
    private String step;
    /**
     * 最后更新时间
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
