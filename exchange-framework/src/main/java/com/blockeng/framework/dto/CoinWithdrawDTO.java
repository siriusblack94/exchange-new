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
public class CoinWithdrawDTO implements Serializable {

    private static final long serialVersionUID = -7236611201976949014L;

    /**
     * 自增id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 币种id
     */
    private Long coinId;

    /**
     * 币种名称
     */
    private String coinName;

    /**
     * 币种类型
     */
    private String coinType;

    /**
     * 钱包标志
     */
    private String walletName;

    /**
     * 钱包地址
     */
    private String address;

    /**
     * 交易id
     */
    private String txid;

    /**
     * 提现量
     */
    private BigDecimal num;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 实际提现
     */
    private BigDecimal mum;

    /**
     * 0站内1其他
     */
    private Integer type;

    /**
     * 区块高度
     */
    private Integer blockNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 当前审核级数
     */
    private Integer step;

    /**
     * 0：审核中；1：已转出；2：撤销；3：打币中；
     */
    private Integer status;

    /**
     * 修改时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;
}
