package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 币种配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("coin_config")
public class CoinConfig extends Model<CoinConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 币种名称
     */
    private String name;
    /**
     * xnb：人民币
     * btc：比特币系列
     * ETH：以太坊
     * ethToken：以太坊代币
     */
    @TableField("coin_type")
    private String coinType;

    /**
     * 打款钱包最低留存币的数量
     */
    @TableField("credit_limit")
    private BigDecimal creditLimit;
    /**
     * 归账额度
     */
    @TableField("credit_max_limit")
    private BigDecimal creditMaxLimit;
    /**
     * 自动提币
     */
    @TableField("auto_recharge")
    private Integer autoRecharge;
    /**
     * 自动提币
     */
    @TableField("auto_draw")
    private Integer autoDraw;
    /**
     * 自动归集
     */
    @TableField("auto_collect")
    private Integer autoCollect;
    /**
     * 自动生成地址
     */
    @TableField("auto_address")
    private Integer autoAddress;
    /**
     * rpc服务ip
     */
    @TableField("rpc_ip")
    private String rpcIp;

    /**
     * rpc服务port
     */
    @TableField("rpc_port")
    private String rpcPort;

    /**
     * rpc用户
     */
    @TableField("rpc_user")
    private String rpcUser;

    /**
     * rpc密码
     */
    @TableField("rpc_pwd")
    private String rpcPwd;

    /**
     * 转出rpc服务ip
     */
    @TableField("rpc_ip_out")
    private String rpcIpOut;

    /**
     * 转出rpc服务port
     */
    @TableField("rpc_port_out")
    private String rpcPortOut;

    /**
     * 转出rpc用户
     */
    @TableField("rpc_user_out")
    private String rpcUserOut;

    /**
     * 转出rpc密码
     */
    @TableField("rpc_pwd_out")
    private String rpcPwdOut;

    /**
     * 最后一个区块
     */
    @TableField("last_block")
    private String lastBlock;

    /**
     * 钱包用户名
     */
    @TableField("wallet_user")
    private String walletUser;
    /**
     * 钱包密码
     */
    @TableField("wallet_pass")
    private String walletPass;

    /**
     * 转出钱包用户名
     */
    @TableField("wallet_user_out")
    private String walletUserOut;
    /**
     * 转出钱包密码
     */
    @TableField("wallet_pass_out")
    private String walletPassOut;

    /**
     * 代币合约地址
     */
    @TableField("contract_address")
    private String contractAddress;

    /**
     * 最低确认数
     */
    @TableField("min_confirm")
    private Integer minConfirm;


    /**
     * 上下文
     */
    @TableField("context")
    private String context;

    /**
     * 定时任务
     */
    private String task;

    /**
     * 是否可用0不可用,1可用
     */
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinConfig coin = (CoinConfig) o;
        return id == coin.id;
    }

    @Override
    public String toString() {
        return "CoinConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coinType='" + coinType + '\'' +
                ", creditLimit=" + creditLimit +
                ", creditMaxLimit=" + creditMaxLimit +
                ", autoRecharge=" + autoRecharge +
                ", autoDraw=" + autoDraw +
                ", autoCollect=" + autoCollect +
                ", autoAddress=" + autoAddress +
                ", rpcIp='" + rpcIp + '\'' +
                ", rpcPort='" + rpcPort + '\'' +
                ", rpcUser='" + rpcUser + '\'' +
                ", rpcPwd='" + rpcPwd + '\'' +
                ", rpcIpOut='" + rpcIpOut + '\'' +
                ", rpcPortOut='" + rpcPortOut + '\'' +
                ", rpcUserOut='" + rpcUserOut + '\'' +
                ", rpcPwdOut='" + rpcPwdOut + '\'' +
                ", lastBlock='" + lastBlock + '\'' +
                ", walletUser='" + walletUser + '\'' +
                ", walletPass='" + walletPass + '\'' +
                ", walletUserOut='" + walletUserOut + '\'' +
                ", walletPassOut='" + walletPassOut + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", minConfirm=" + minConfirm +
                ", context='" + context + '\'' +
                ", task='" + task + '\'' +
                ", status=" + status +
                '}';
    }
}
