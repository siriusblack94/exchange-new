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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
@TableName("work_issue")
@ApiModel(description = "工单记录")
public class WorkIssue extends Model<WorkIssue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键id", required = false)
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 提问用户id
     */
    @ApiModelProperty(value = "提问用户id", required = true)
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 回复用户id
     */
    @ApiModelProperty(value = "回复用户id", required = false)
    @TableField("answer_user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long answerUserId;

    /**
     * 回复用户名称
     */
    @ApiModelProperty(value = "回复用户名称", required = false)
    @TableField("answer_name")
    private String answerName;

    /**
     * 工单内容
     */
    @ApiModelProperty(value = "工单内容", required = false)
    private String question;
    /**
     * 回答内容
     */
    @ApiModelProperty(value = "回答内容", required = true)
    private String answer;
    /**
     * 状态：1-待回答；2-已回答；
     */
    @ApiModelProperty(value = "状态：1-待回答；2-已回答；", required = true)
    private Integer status;
    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", required = false)
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = false)
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
