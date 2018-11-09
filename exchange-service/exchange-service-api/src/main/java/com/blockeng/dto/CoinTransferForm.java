package com.blockeng.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: jakiro
 * @Date: 2018-11-02 10:09
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CoinTransferForm implements Serializable {


    @NotEmpty(message = "打款人不能为空")
    @ApiModelProperty(value = "打款人ID", name = "moneyMakerUserId")
    private Long moneyMakerUserId;


    @NotEmpty(message = "币种不能为空")
    @ApiModelProperty(value = "币种ID", name = "coinId")
    private Long coinId;


    @NotEmpty(message = "转帐金额不能为空")
    @ApiModelProperty(value = "转帐额度", name = "num")
    private BigDecimal num;


    @NotEmpty(message = "收款人不能为空")
    @ApiModelProperty(value = "收款人ID", name = "payeeUserId")
    private Long payeeUserId;


    @NotEmpty(message = "交易密码不能为空")
    @ApiModelProperty(value = "交易密码", name = "payPassWord")
    private String payPassWord;


    @NotEmpty(message = "校验码不能为空")
    @ApiModelProperty(value = "校验码", name = "validateCode")
    private String validateCode;


    @ApiModelProperty(value = "国际电话区号", name = "countryCode")
    private String countryCode;


    @ApiModelProperty(value = "电话号", name = "mobile")
    private String mobile="";


    @ApiModelProperty(value = "邮箱", name = "email")
    private String email="";
}
