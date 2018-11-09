package com.blockeng.framework.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/21 下午7:34
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CoinRechargeDTO implements Serializable {

    private static final long serialVersionUID = -2731369102562286192L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 币种id
     */
    private Long coinId;

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 交易id
     */
    private String txid;

    /**
     * 实际到账
     */
    private BigDecimal amount;

    /**
     * 交易的区块高度
     */
    private Integer blockNumber;

    /**
     * 0正常-1充值失败
     */
    private Integer type;

    /**
     * 0不是1是
     */
    private Integer isFee;

    /**
     * 修改时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;
}
