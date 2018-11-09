package com.blockeng.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBankForm implements java.io.Serializable {


    /**
     * ID
     */
    private long id;

    /**
     * 姓名
     */
    @NotEmpty(message = "用户姓名")
    private String realName;
    /**
     * 开户行名称
     */
    @NotEmpty(message = "开户行名称")
    private String bank;
    /**
     * 卡号
     */
    @NotEmpty(message = "卡号")
    private String bankCard;

    /**
     * 支付密码
     */
    @NotEmpty(message = "支付密码")
    private String payPassword;

    /**
     * 备注信息
     */
    private String remark;
}