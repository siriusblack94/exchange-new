package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: jakiro
 * @Date: 2018-11-05 16:39
 * @Description: 站内转帐明细
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CoinTransferDTO {

    /**
     * 转帐时间
     * */
    private Date date;

    /**
     * 转帐数量
     * */
    private BigDecimal transferNum;

    /**
     * 手续费
     * */
    private BigDecimal fee=new BigDecimal(0);

    /**
     * 方式 1:转出 2:转入
     * */
    private int direction;

    /**
     * 状态 0:失败 1:成功
     * */
    private int transferStatus=1;


    /**
     * 币种名称
     * */
    private String coinName;

}
