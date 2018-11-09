package com.blockeng.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户人民币提现地址
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("user_bank")
public class UserBank extends Model<UserBank> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 银行卡名称
     */
    private String remark;
    /**
     * 开户人
     */
    @TableField("real_name")
    private String realName;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 开户省
     */
    @TableField("bank_prov")
    private String bankProv;
    /**
     * 开户市
     */
    @TableField("bank_city")
    private String bankCity;
    /**
     * 开户地址
     */
    @TableField("bank_addr")
    private String bankAddr;
    /**
     * 开户账号
     */
    @TableField("bank_card")
    private String bankCard;
    /**
     * 状态：0，禁用；1，启用；
     */
    private Integer status;
    /**
     * 更新时间
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
