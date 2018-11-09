package com.blockeng.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 工单记录
 * </p>
 *
 * @author qiang
 * @since 2018-05-31
 */
@Data
@Accessors(chain = true)
@TableName("work_issue")
public class WorkIssue extends Model<WorkIssue> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id(提问用户id)
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 回复人id
     */
    @TableField("answer_user_id")
    private Long answerUserId;
    /**
     * 回复人名称
     */
    @TableField("answer_name")
    private String answerName;
    /**
     * 工单内容
     */
    private String question;
    /**
     * 回答内容
     */
    private String answer;
    /**
     * 状态：1-待回答；2-已回答；
     */
    private Integer status;
    /**
     * 修改时间
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
