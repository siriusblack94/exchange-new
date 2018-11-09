package com.blockeng.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户充值,当前用户充值成功之后添加数据到这个表,充值一般无手续费.当status为1的时候表示充值成功
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@TableName("coin_server")
public class CoinServer extends Model<CoinServer> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 服务器ip
     */
    @TableField("rpc_port")
    private String rpcPort;
    /**
     * 服务器端口
     */
    @TableField("rpc_ip")
    private String rpcIp;
    /**
     * 服务是否运行
     */
    private int running;

    /**
     * 钱包区块高度
     */
    @TableField("wallet_number")
    private Integer walletNumber;

    /**
     * 真实高度,来源于区块浏览器
     */
    @TableField("real_number")
    private Integer realNumber;

    /**
     * 钱包名称
     */
    @TableField("coin_name")
    private String coinName;

    /**
     * 标记
     */
    private String mark;

    /**
     * 修改时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
