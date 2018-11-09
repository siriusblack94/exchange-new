package com.blockeng.admin.dto;

import com.blockeng.admin.entity.CoinRecharge;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * Create Time: 2018年05月20日 15:18
 * C@author lxl
 **/
@Data
@Accessors(chain = true)
public class MakeUpRechargeDTO {

    /**
     * 充值地址
     */
    @NotEmpty(message = "充值地址")
    private String address;

    /**
     * 订单id
     */
    @NotEmpty(message = "充值的订单id")
    private String txId;

}
