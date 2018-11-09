package com.blockeng.admin.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
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
 * @since 2018-05-17
 */
@Data
@Accessors(chain = true)
@TableName("coin_config")
public class CoinConfig extends Model<CoinConfig> {

    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     * 对应coin表ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 币种名称
     */
    @ApiModelProperty(value = "姓名", name = "name", example = "", required = true)
    private String name;
    /**
     * xnb：人民币
     * default：比特币系列
     * ETH：以太坊
     * ethToken：以太坊代币
     */
    @TableField("coin_type")
    @ApiModelProperty(value = "类型", name = "coinType", example = "xnb 人民币,default 比特币系列,ETH 以太坊,ethToken以太坊代币", required = true)
    private String coinType;

    @TableField("credit_limit")
    @ApiModelProperty(value = "最低保留额度(信用额度)", name = "coinType", example = "123.88", required = false)
    private BigDecimal creditLimit;


    @TableField("credit_max_limit")
    @ApiModelProperty(value = "归账最大额度", name = "coinType", example = "123.88", required = false)
    private BigDecimal creditMaxLimit;

    /**
     * 是否自动打款
     */
    @TableField("auto_draw")
    @ApiModelProperty(value = "自动打币", name = "autoDraw", example = "1", required = false)
    private Integer autoDraw;

    /**
     * 是否自动打款
     */
    @TableField("auto_address")
    @ApiModelProperty(value = "自动地址", name = "autoAddress", example = "1", required = false)
    private Integer autoAddress;

    /**
     * 是否自动打款
     */
    @TableField("auto_recharge")
    @ApiModelProperty(value = "自动充值", name = "autoRecharge", example = "1", required = false)
    private Integer autoRecharge;
    /**
     * 是否自动打款
     */
    @TableField("auto_collect")
    @ApiModelProperty(value = "自动归集", name = "autoCollect", example = "1", required = false)
    private Integer autoCollect;

    /**
     * 自动打款最高额度
     */
    @TableField("auto_draw_limit")
    @ApiModelProperty(value = "自动打款最高额度", name = "autoDrawLimit", example = "1", required = false)
    private BigDecimal autoDrawLimit;
    /**
     * rpc服务ip
     */
    @TableField("rpc_ip")
    @ApiModelProperty(value = "rpc服务ip", name = "rpcIp", example = "", required = true)
    private String rpcIp;
    /**
     * rpc服务port
     */
    @TableField("rpc_port")
    @ApiModelProperty(value = "rpc服务port", name = "rpcPort", example = "", required = true)
    private String rpcPort;
    /**
     * rpc用户
     */
    @TableField("rpc_user")
    @ApiModelProperty(value = "rpc用户名", name = "rpcUser", example = "", required = true)
    private String rpcUser;
    /**
     * rpc密码
     */
    @TableField("rpc_pwd")
    @ApiModelProperty(value = "rpc密码", name = "rpcPwd", example = "", required = true)
    private String rpcPwd;
    /**
     * 地址rpc服务ip
     */
    @TableField("rpc_ip_out")
    @ApiModelProperty(value = "地址rpc服务ip", name = "rpcIpOut", example = "", required = true)
    private String rpcIpOut;

    /**
     * 地址rpc服务port
     */
    @TableField("rpc_port_out")
    @ApiModelProperty(value = "地址rpc服务port", name = "rpcPortOut", example = "", required = true)
    private String rpcPortOut;

    /**
     * 地址rpc用户
     */
    @TableField("rpc_user_out")
    @ApiModelProperty(value = "地址rpc用户名", name = "rpcUserOut", example = "", required = true)
    private String rpcUserOut;

    /**
     * 地址rpc密码
     */
    @TableField("rpc_pwd_out")
    @ApiModelProperty(value = "地址rpc密码", name = "rpcPwdOut", example = "", required = true)
    private String rpcPwdOut;
    /**
     * 最后一个区块
     */
    @TableField("last_block")
    @ApiModelProperty(value = "最后一个区块", name = "lastBlock", example = "", required = true)
    private String lastBlock;
    /**
     * 代币合约地址
     */
    @TableField("contract_address")
    @ApiModelProperty(value = "代币合约地址", name = "contractAddress", example = "", required = true)
    private String contractAddress;
    /**
     * 最低确认数
     */
    @TableField("min_confirm")
    @ApiModelProperty(value = "最低确认数", name = "minConfirm", example = "1", required = true)
    private Integer minConfirm;
    /**
     * 钱包密码
     */
    @TableField("wallet_pass")
    @ApiModelProperty(value = "钱包密码", name = "walletPass", example = "123", required = true)
    private String walletPass;
    /**
     * 地址钱包密码
     */
    @TableField("wallet_pass_out")
    @ApiModelProperty(value = "地址钱包密码", name = "walletPassOut", example = "123", required = true)
    private String walletPassOut;
    /**
     * 定时任务
     */
    @ApiModelProperty(value = "定时任务", name = "task", example = "0/15 * * * * ? ", required = true)
    private String task;
    /**
     * 状态 0不可用,1可用
     */
    @ApiModelProperty(value = "状态:0 不可用 1可用", name = "status", example = "1 ", required = true)
    private Integer status;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
