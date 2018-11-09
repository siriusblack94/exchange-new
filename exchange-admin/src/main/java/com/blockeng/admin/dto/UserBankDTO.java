package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Create Time: 2018年05月15日 19:41
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class UserBankDTO implements Serializable {

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键id", name = "id", required = false)
    private Long id;
    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", required = false)
    private Long userId;

    /**
     * 银行卡名称
     */
    @ApiModelProperty(value = "银行卡名称", name = "remark", required = false)
    private String remark;
    /**
     * 开户人
     */
    @ApiModelProperty(value = "开户人", name = "realName", required = false)

    @TableField("real_name")
    private String realName;
    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行", name = "bank", required = false)

    private String bank;
    /**
     * 开户省
     */
    @ApiModelProperty(value = "开户省", name = "bankProv", required = false)

    @TableField("bank_prov")
    private String bankProv;
    /**
     * 开户市
     */
    @ApiModelProperty(value = "开户市", name = "bankCity", required = false)

    @TableField("bank_city")
    private String bankCity;
    /**
     * 开户地址
     */
    @ApiModelProperty(value = "开户地址", name = "bankAddr", required = false)

    @TableField("bank_addr")
    private String bankAddr;
    /**
     * 开户账号
     */
    @ApiModelProperty(value = "开户账号", name = "bankCard", required = false)

    @TableField("bank_card")
    private String bankCard;
    /**
     * 状态：0，禁用；1，启用；
     */
    @ApiModelProperty(value = " 状态：0，禁用；1，启用；", name = "status", required = false)

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

    /**
     * 用户电话
     */
    @ApiModelProperty(value = " 用户电话", name = "mobile", required = false)

    private String mobile;


}
