package com.blockeng.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class CoinWithdrawModel {

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
     * 钱包地址
     */
    private String address;

    /**
     * 当前审核级数
     */
    private Integer step;

    /**
     * 状态：0-审核中；1-审核通过；2-拒绝；3-提币成功；4：撤销；5-打币中；
     */
    private Integer status;

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
     * 钱包提币备注
     */
    private String walletMark;

}
