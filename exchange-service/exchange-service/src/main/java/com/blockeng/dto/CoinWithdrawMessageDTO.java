package com.blockeng.dto;

import com.blockeng.entity.CoinWithdraw;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description: 提币结果消息
 * @Author: Chen Long
 * @Date: Created in 2018/6/28 下午7:42
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CoinWithdrawMessageDTO {

    /**
     * 响应代码
     */
    private int statusCode;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 响应数据对象
     */
    private CoinWithdraw result;
}
