package com.blockeng.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

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
public class CoinDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 币种ID
     */
    private Long id;

    /**
     * 币种名称
     */
    private String name;

    /**
     * 币种标题
     */
    private String title;

    /**
     * 币种logo
     */
    private String img;

    /**
     * default：比特币系列;ETH：以太坊;ethToken：以太坊代币
     */
    private String type;

    /**
     * rgb：认购币; qbb：钱包币
     */
    private String wallet;

    /**
     * 小数位数
     */
    private Integer round;

    /**
     * status=1：启用; 0：禁用
     */
    private Integer status;

    /**
     * 自动转出数量
     */
    private Double autoOut;

    /**
     * 手续费率
     */
    private Double rate;

    /**
     * 最低收取手续费个数
     */
    private BigDecimal minFeeNum;

    /**
     * 提现开关
     */
    private Integer withdrawFlag;

    /**
     * 充值开关
     */
    private Integer rechargeFlag;

    /**
     * 更新时间
     */
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    private Date created;
}
