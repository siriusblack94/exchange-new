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
 * @Auther: sirius
 * @Date: 2018年8月15日13:04:22
 * @Description:私募类
 */


@Data
@Accessors(chain = true)
@TableName("private_placement")
public class PrivatePlacement extends Model<PrivatePlacement> {
  private static final long serialVersionUID = 1L;
  /**
   * 描述: 主键
   * @auther: sirius
   * @date: 2018年8月15日13:09:20
   */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @TableField("user_id")
    private Long userId;
    /**
     * 描述: 私募金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    private BigDecimal amount;
    /**
     * 描述: 私募冻结金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("freeze_amount")
    private BigDecimal freezeAmount;

    /**
     * 描述: 私募释放金额
     * @auther: sirius
     * @date: 2018年8月15日13:09:20
     */
    @TableField("release_amount")
    private BigDecimal releaseAmount;


    private Date created;

    @TableField("last_update_time")
    private Date lastUpdateTime;


  @Override
  protected Serializable pkVal() {
    return this.id;
  }
}
