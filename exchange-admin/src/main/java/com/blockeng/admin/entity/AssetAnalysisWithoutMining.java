package com.blockeng.admin.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author: jakiro
 * @Date: 2018-10-14 18:56
 * @Description: 资产分析实体类(非挖矿)
 * (数字币充值+场外充值+资产转移+充值+后台充值)+(交易收入-交易支出-买手续费-卖手续费)-(数字币提取+场外提现)+补-扣=(总余额+冻结)
 */
@Data
@Accessors(chain = true)
public class AssetAnalysisWithoutMining {

      @ApiModelProperty(value = "币种名称", name = "coinName", required = true)
      private String coinName="";

      @ApiModelProperty(value = "数字币充值金额", name = "coinRecharge", required = true)
      private BigDecimal coinRecharge=new BigDecimal(0);

      @ApiModelProperty(value = "法币充值金额", name = "cashRecharge", required = true)
      private BigDecimal cashRecharge=new BigDecimal(0);

      @ApiModelProperty(value = "总充值金额", name = "totalRecharge", required = true)
      private BigDecimal totalRecharge=new BigDecimal(0);

      @ApiModelProperty(value = "资产转移", name = "assetTransfer", required = true)
      private BigDecimal assetTransfer=new BigDecimal(0);

      @ApiModelProperty(value = "充值", name = "recharge", required = true)
      private BigDecimal recharge=new BigDecimal(0);

      @ApiModelProperty(value = "后台充值", name = "backstageRecharge", required = true)
      private BigDecimal backstageRecharge=new BigDecimal(0);

      @ApiModelProperty(value = "交易收入", name = "exchangeIncome", required = true)
      private BigDecimal exchangeIncome=new BigDecimal(0);

      @ApiModelProperty(value = "买入手续费", name = "buyFee", required = true)
      private BigDecimal buyFee=new BigDecimal(0);

      @ApiModelProperty(value = "交易支出", name = "exchangeExpend", required = true)
      private BigDecimal exchangeExpend=new BigDecimal(0);

      @ApiModelProperty(value = "卖出手续费", name = "sellFee", required = true)
      private BigDecimal sellFee=new BigDecimal(0);

      @ApiModelProperty(value = "数字币提取", name = "coinWithdraw", required = true)
      private BigDecimal coinWithdraw=new BigDecimal(0);

      @ApiModelProperty(value = "场外提取", name = "cashWithdraw", required = true)
      private BigDecimal cashWithdraw=new BigDecimal(0);

      @ApiModelProperty(value = "补进的钱", name = "supply", required = true)
      private BigDecimal supply=new BigDecimal(0);

      @ApiModelProperty(value = "扣除的钱", name = "deduct", required = true)
      private BigDecimal deduct=new BigDecimal(0);

      @ApiModelProperty(value = "当前总余额", name = "balance", required = true)
      private BigDecimal balance=new BigDecimal(0);

      @ApiModelProperty(value = "总冻结", name = "freeze", required = true)
      private BigDecimal freeze=new BigDecimal(0);

      @ApiModelProperty(value = "交易冻结", name = "exchangeFreeze", required = true)
      private BigDecimal exchangeFreeze=new BigDecimal(0);

      @ApiModelProperty(value = "数字币提现冻结", name = "exchangeFreeze", required = true)
      private BigDecimal coinWithdrawFreeze=new BigDecimal(0);

      @ApiModelProperty(value = "场外提现冻结", name = "exchangeFreeze", required = true)
      private BigDecimal cashWithdrawFreeze=new BigDecimal(0);

      @ApiModelProperty(value = "补扣冻结", name = "exchangeFreeze", required = true)
      private BigDecimal buckleFreeze=new BigDecimal(0);


}
