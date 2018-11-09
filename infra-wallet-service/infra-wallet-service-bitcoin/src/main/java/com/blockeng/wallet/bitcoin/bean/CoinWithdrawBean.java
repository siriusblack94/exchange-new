package com.blockeng.wallet.bitcoin.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class CoinWithdrawBean {
    /**
     * 自增id
     */
    public Long id;
    /**
     * 用户id
     */
    public Long userId;
    /**
     * 币种id
     */
    public Long coinId;
    /**
     * 币种名称
     */
    public String coinName;
    /**
     * 币种类型
     */
    public String coinType;
    /**
     * 钱包地址
     */
    public String address;
    /**
     * 0：审核中；1：已转出；2：拒绝；3：撤销；4,5：打币中 5(代表钱包打币成功)；
     */
    public Integer status;
    /**
     * 交易id
     */
    public String txid;
    /**
     * 提现量
     */
    public BigDecimal num;
    /**
     * 手续费
     */
    public BigDecimal fee;
    /**
     * 实际提现
     */
    public BigDecimal mum;
    /**
     * 0站内1其他
     */
    public Integer type;

    /**
     * 0站内1其他
     */
    public BigDecimal chainFee;

    /**
     * 区块高度
     */
    public Integer blockNum;
    /**
     * 备注
     */
    public String remark;
    /**
     * 备注
     */
    public String walletMark;

}
