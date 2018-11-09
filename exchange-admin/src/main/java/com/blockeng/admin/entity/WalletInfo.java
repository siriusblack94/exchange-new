package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: sirius
 * @Date: 2018/9/29 14:54
 * @Description:
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("wallet_info")
public class WalletInfo {
    @ApiModelProperty(name = "id", value = "主键", example = "主键")
    Long id;

    @ApiModelProperty(name = "coinId", value = "币种ID", example = "币种ID")
    @TableField("coin_id")
    Long coinId;

    @ApiModelProperty(name = "walletBalanceTotal", value = "钱包余额", example = "钱包余额")
    @TableField("wallet_balance_total")
    BigDecimal walletBalanceTotal;

    @ApiModelProperty(name = "balanceTotal", value = "系统真实用户持币总额", example = "系统真实用户持币总额")
    @TableField("balance_total")
    BigDecimal balanceTotal;

    @ApiModelProperty(name = "walletUserCount", value = "钱包持币用户数量", example = "钱包持币用户数量")
    @TableField("wallet_user_count")
    Integer walletUserCount;

    @ApiModelProperty(name = "userCount", value = "系统真实持币用户数量", example = "系统真实持币用户数量")
    @TableField("user_count")
    Integer userCount;

    @ApiModelProperty(name = "name", value = "币种名称", example = "币种名称")
    String name;

    @ApiModelProperty(name = "coinRechargeAmount", value = "充币数量", example = "充币数量")
    @TableField("coin_recharge_amount")
    BigDecimal coinRechargeAmount;

    @ApiModelProperty(name = "coinRechargeAmount", value = "提币数量", example = "提币数量")
    @TableField("coin_withdraw_amount")
    BigDecimal coinWithdrawAmount;

    @ApiModelProperty(name = "balanceTotalIn", value = "转入地址钱包余额", example = "转入地址钱包余额")
    @TableField("balance_total_in")
    BigDecimal balanceTotalIn;

    @ApiModelProperty(name = "balanceTotalOut", value = "转出地址钱包余额", example = "转出地址钱包余额")
    @TableField("balance_total_out")
    BigDecimal balanceTotalOut;

    @ApiModelProperty(name = "date", value = "存入数据库时间", example = "存入数据库时间")
    Date date;
    @ApiModelProperty(name = "created", value = "开始统计时间", example = "开始统计时间")
    Date created;
}
