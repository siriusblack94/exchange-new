package com.blockeng.admin.entity;


import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * @Auther: sirius
 * @Date: 2018/10/25 11:16
 * @Description:
 */
@Data
@Accessors(chain = true)
@TableName("points")
public class Points  {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */

    @TableField("user_id")
    private String userId;
    /**
     * 币种类型  0 eth 1 usdt 2 btc 3 gtb
     */
    private String type;

    /**
     * 数量
     */
    private String count;

    /**
     * 加减 0加1减
     */
    private String plusOrMinus="1";

    /**
     * 签名
     */
    private String sign;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 币种id
     */
    @TableField("coin_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long coinId;

    @TableField("coin_name")
    private String coinName;

    private Integer status=0;

    private String message="等待兑换";

    private String remark="0"; //0 等待兑换 1余额不足 2余额冻结成功 3兑换系统异常 4资金扣减(增加)异常 5兑换成功 6资金解冻成功

    private String returnurl;

    private Date created = new Date();

    @TableField(exist = false)
    private String email;

    @TableField(exist = false)
    @ApiModelProperty(value="手机号",name ="mobile" ,required = true)
    private String mobile;
    @TableField(exist = false)
    @ApiModelProperty(value="真实姓名",name ="realName" ,required = true)
    private String realName;
    @TableField(exist = false)
    @ApiModelProperty(value="用户名",name ="userName" ,required = true)
    private String username;

}
