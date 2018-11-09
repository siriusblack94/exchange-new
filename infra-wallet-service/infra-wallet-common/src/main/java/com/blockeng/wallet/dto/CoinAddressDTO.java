package com.blockeng.wallet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/25 下午2:52
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CoinAddressDTO {

    /**
     * 币种名称
     */
    @NotEmpty(message = "币种id")
    private String coinId;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;
//
//    /**
//     * 时间戳
//     */
//    @NotNull(message = "时间戳不能为空")
//    private Long timestamp;
//
//    /**
//     * 签名
//     */
//    @NotEmpty(message = "签名不能为空")
//    private String sign;
}
