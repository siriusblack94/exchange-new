package com.blockeng.admin.dto;


import com.baomidou.mybatisplus.plugins.Page;
import com.blockeng.admin.entity.AccountBalance;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AccountBalanceStatiscDTO {


     @ApiModelProperty(value = "总资产", name = "totalBalance", required = false)
     private BigDecimal totalBalance=new BigDecimal(0);

     @ApiModelProperty(value = "可用余额", name = "availableBalance", required = false)
     private BigDecimal availableBalance=new BigDecimal(0);

     @ApiModelProperty(value = "交易冻结", name = "transactionFreeze", required = false)
     private BigDecimal transactionFreeze=new BigDecimal(0);

     @ApiModelProperty(value = "提现冻结", name = "withdrawFreeze", required = false)
     private BigDecimal withdrawFreeze=new BigDecimal(0);

     @ApiModelProperty(value = "补扣冻结", name = "buckleFreeze", required = false)
     private BigDecimal buckleFreeze=new BigDecimal(0);

     @ApiModelProperty(value = "钱包类型", name = "walletType", required = false)
     private String walletType;

     @ApiModelProperty(value = "列表", name = "records", required = false)
     private List<AccountBalance> records=new ArrayList<>();

}
