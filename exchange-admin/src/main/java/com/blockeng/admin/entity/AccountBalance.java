package com.blockeng.admin.entity;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountBalance {

    @ApiModelProperty(value = "用户类型", name = "userType", required = false)
    private String userType="";

    @ApiModelProperty(value = "用户数量", name = "userCount", required = false)
    private Integer userCount=0;

    @ApiModelProperty(value = "钱包类型", name = "walletType", required = false)
    private String walletType="";

    @ApiModelProperty(value = "总资产", name = "totalBalance", required = false)
    private BigDecimal totalBalance=new BigDecimal(0);

    @ApiModelProperty(value = "可用余额", name = "totalBalance", required = false)
    private BigDecimal availableBalance=new BigDecimal(0);

    @ApiModelProperty(value = "交易冻结", name = "totalBalance", required = false)
    private BigDecimal transactionFreeze=new BigDecimal(0);

    @ApiModelProperty(value = "提现冻结", name = "withdrawFreeze", required = false)
    private BigDecimal withdrawFreeze=new BigDecimal(0);

    @ApiModelProperty(value = "补扣冻结", name = "buckleFreeze", required = false)
    private BigDecimal buckleFreeze=new BigDecimal(0);

    private Integer userFlag;

}
