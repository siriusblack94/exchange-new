package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户注册统计DTO
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class UserCountRegDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计日期", name = "countDate", required = false)
   //@TableField("count_date")
    private String countDate;

    @ApiModelProperty(value = "注册人数", name = "regNum", required = false)
  //  @TableField("reg_num")
    private Integer regNum;

    @ApiModelProperty(value = "绑定手机人数", name = "mobileBindNum", required = false)
    @TableField(exist = false)
    private Integer mobileBindNum;

    @ApiModelProperty(value = "绑定邮箱人数", name = "emailBindNum", required = false)
    @TableField(exist = false)
    private Integer emailBindNum;

    @ApiModelProperty(value = "设置资金密码人数", name = "setPayPwdNum", required = false)
    @TableField(exist = false)
    private Integer setPayPwdNum;

    @ApiModelProperty(value = "完成充值人数", name = "rechargeNum", required = false)
    @TableField(exist = false)
    private Integer rechargeNum;


}
