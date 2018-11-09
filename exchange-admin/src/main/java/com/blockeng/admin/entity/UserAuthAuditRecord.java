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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 实名认证审核信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("user_auth_audit_record")
public class UserAuthAuditRecord extends Model<UserAuthAuditRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "id", name = "id", example = "", required = false)

    private Long id;
    /**
     * 用户ID
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @TableField("auth_code")
    @ApiModelProperty(value = "实名认证图片code", name = "authCode", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long authCode;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态1通过2拒绝", name = "status", example = "", required = false)
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark", example = "", required = false)

    private String remark;
    /**
     * 当前审核级数
     */
    @ApiModelProperty(value = "当前审核级数", name = "step", example = "", required = false)

    private Integer step;
    /**
     * 审核人ID
     */
    @TableField("audit_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "审核人ID", name = "auditUserId", example = "", required = false)

    private Long auditUserId;
    /**
     * 审核人ID
     */
    @TableField("audit_user_name")
    @ApiModelProperty(value = "审核人名称", name = "auditUserName", example = "", required = false)

    private String auditUserName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "created", example = "", required = false)

    private Date created;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
