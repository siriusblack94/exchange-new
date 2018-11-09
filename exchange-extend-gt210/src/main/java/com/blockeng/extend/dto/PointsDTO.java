package com.blockeng.extend.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.blockeng.extend.function.GroupInterface;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Auther: sirius
 * @Date: 2018/10/25 17:32
 * @Description:
 */
@Data
@Accessors(chain = true)
public class PointsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 订单唯一ID
     */
    @TableField(exist = false)
    private String orderId;
    /**
     * 用户ID
     */
    @NotBlank(message = "不能为空",groups = {GroupInterface.class})
    @TableField("user_id")
    private String userId;
    /**
     * 币种类型  0 eth 1 usdt 2 btc 3 gtb
     */
    @NotBlank(message = "不能为空",groups = {GroupInterface.class})
    private String type;

    /**
     * 数量
     */
    @NotBlank (message = "不能为空",groups = {GroupInterface.class})
    private String count;

    /**
     * 加减 0加1减
     */
    private String plusOrMinus="1";

    /**
     * 签名
     */
    private String sign;


}
