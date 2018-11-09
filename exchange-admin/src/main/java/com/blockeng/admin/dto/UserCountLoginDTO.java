package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 用户登陆统计DTO
 * </p>
 *
 * @author Haliyo
 * @since 2018-05-13
 */
@Data
@Accessors(chain = true)
public class UserCountLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登陆日期", name = "loginDate", required = false)
    @TableField("login_date")
    private String loginDate;

    @ApiModelProperty(value = "登陆人数", name = "loginNum", required = false)
    @TableField(value = "login_num", exist = false)
    private Integer loginNum;

    @ApiModelProperty(value = "参与交易人数", name = "tradeNum", required = false)
    @TableField(exist = false)
    private Integer tradeNum;

    @ApiModelProperty(value = "充值人数", name = "rechargeNum", required = false)
    @TableField(exist = false)
    private Integer rechargeNum;

    @ApiModelProperty(value = "提现人数", name = "withdrawNum", required = false)
    @TableField(exist = false)
    private Integer withdrawNum;

    @ApiModelProperty(value = "充币人数", name = "rechargeNum", required = false)
    @TableField(exist = false)
    private Integer rechargeCoinNum;

    @ApiModelProperty(value = "提币人数", name = "withdrawNum", required = false)
    @TableField(exist = false)
    private Integer withdrawCoinNum;


}
