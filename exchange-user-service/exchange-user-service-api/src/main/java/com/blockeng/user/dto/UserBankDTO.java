package com.blockeng.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 用户人民币提现地址
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用户人民币提现地址", description = "用户人民币提现地址")
public class UserBankDTO implements java.io.Serializable {

    /**
     * 自增id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 用户id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     * 币种id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;
    /**
     * 币种名称
     */
    private String coinName;
    /**
     * 银行卡名称
     */
    private String remark;
    /**
     * 开户人
     */
    private String realName;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 开户省
     */
    private String bankProv;
    /**
     * 开户市
     */
    private String bankCity;
    /**
     * 开户地址
     */
    private String bankAddr;
    /**
     * 开户账号
     */
    private String bankCard;
    /**
     * 状态：0，禁用；1，启用；
     */
    private Integer status;
    /**
     * 更新时间
     */
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;
}
