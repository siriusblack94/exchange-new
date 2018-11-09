package com.blockeng.admin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class CoinWithdrawRetryRecordDTO {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long orderId;
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
     * 0站内1站外自动2站外手工提币
     */
    private Integer type;

    /**
     * 审核备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date created;

    /**
     * 一级审核人员id
     */
    private Long auditUserId;


    /**
     * 一级审核人员姓名
     */
    private String auditUserName;

    /**
     * 二级审核人员id
     */
    private Long secondAuditUserId;


    /**
     * 二级审核人员姓名
     */
    private String secondAuditUserName;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 审核级别
     */
    private Integer step;

}
