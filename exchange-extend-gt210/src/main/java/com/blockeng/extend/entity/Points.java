package com.blockeng.extend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.blockeng.extend.dto.PointsDTO;
import com.blockeng.extend.function.GroupInterface1;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @Auther: sirius
 * @Date: 2018/10/25 11:16
 * @Description:
 */
@Data
@Accessors(chain = true)
@TableName("points")
public class Points extends PointsDTO {
    private static final long serialVersionUID = 1L;

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

    private String remark="0"; //0 等待兑换 1余额不足 2余额冻结成功 3兑换系统异常 4资金扣减(增加)异常 5兑换成功

    private String returnurl;

    private Date created = new Date();

    /**
     * 短信验证码
     */
    @NotBlank(message = "短信验证码不能为空",groups = {GroupInterface1.class})
    @TableField(exist = false)
    private String validateCode;


    @NotBlank(message = "交易密码不能为空",groups = {GroupInterface1.class})
    @TableField(exist = false)
    private String payPassword;

    @TableField(exist = false)
    private String email;

    @Override
    public String toString() {
        return "Points{" +
                "id=" + id +
                ", coinId=" + coinId +
                '}'+super.toString();
    }


}
