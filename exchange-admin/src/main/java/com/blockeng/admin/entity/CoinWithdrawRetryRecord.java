package com.blockeng.admin.entity;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 重新打币记录
 *
 */
@Data
@Accessors(chain = true)
@TableName("coin_withdraw_retry_record")
public class CoinWithdrawRetryRecord {


    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 订单id
     */
    @TableField("order_id")
    @ApiModelProperty(value = "订单id", name = "orderId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ApiModelProperty(value = "用户id", name = "userId", example = "", required = false)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    /**
     * 币种名称
     */
    @TableField("coin_name")
    @ApiModelProperty(value = "币种名称", name = "coinName", example = "", required = false)
    private String coinName;


    /**
     * 钱包地址
     */
    @ApiModelProperty(value = "钱包地址", name = "address", example = "", required = false)
    private String address;

    /**
     * 交易id
     */
    @ApiModelProperty(value = "交易id", name = "txid", example = "", required = false)
    private String txid;
    /**
     * 提现量
     */
    @ApiModelProperty(value = "提现量", name = "num", example = "", required = false)
    private BigDecimal num;
    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费", name = "fee", example = "", required = false)
    private BigDecimal fee;
    /**
     * 实际提现
     */
    @ApiModelProperty(value = "提现金额", name = "mum", example = "", required = false)
    private BigDecimal mum;
    /**
     * 0站内1站外自动2站外手工提币
     */
    private Integer type;

    /**
     * 审核备注
     */
    @ApiModelProperty(value = "后台审核人员提币备注备注", name = "remark", example = "", required = true)
    private String remark;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "申请时间", name = "created", example = "", required = false)
    private Date created;

    /**
     * 一级审核人员id
     */
    @TableField(value = "first_audit_user_id", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String firstAuditUserId;

    /**
     * 一级审核人员姓名
     */
    @TableField(value = "first_audit_user_name", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String firstAuditUserName;

    /**
     * 二级审核人员id
     */
    @TableField(value = "second_audit_user_id", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String secondAuditUserId;

    /**
     * 二级审核人员姓名
     */
    @TableField(value = "second_audit_user_name", exist = false)
    @ApiModelProperty(value = "用户名", name = "userName", example = "", required = false)
    private String secondAuditUserName;

    /**
     * 用户手机号
     */
    @TableField(value = "mobile", exist = false)
    @ApiModelProperty(value = "用户手机号", name = "mobile", example = "", required = false)
    private String mobile;

    /**
     * ID字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String idStr;

    /**
     * 创建时间字符串(for导出)
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private String createdStr;
}
