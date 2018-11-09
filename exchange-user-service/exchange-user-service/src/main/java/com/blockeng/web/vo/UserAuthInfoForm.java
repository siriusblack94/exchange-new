package com.blockeng.web.vo;

import com.blockeng.enums.IdCardType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 用户身份认证信息Form
 *
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户身份认证信息")
public class UserAuthInfoForm implements java.io.Serializable {

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", name = "realName", example = "Doug Lea", required = true)
    @NotEmpty(message = "姓名不能为空")
    private String realName;
    /**
     * 证件类型
     * 1-身份证；
     * 2-军官证；
     * 3-护照；
     * 4-台湾居民通行证；
     * 5-港澳居民通行证；
     * 9-其他；
     */
    @ApiModelProperty(value = "证件类型", name = "idCardType", example = "1", required = true)
    @NotNull(message = "证件类型不能为空")
    private Integer idCardType;
    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码", name = "idCard", example = "44030419920919207x", required = true)
    @NotEmpty(message = "证件号码不能为空")
    private String idCard;
    /**
     * 图片地址
     */
    private String imageUrl;

    /**
     * 极验验证二次验证表单数据 chllenge
     */
    @NotEmpty(message = "极验验证二次验证表单数据 chllenge")
    private String geetest_challenge;
    /**
     * 极验验证二次验证表单数据 validate
     */
    @NotEmpty(message = "极验验证二次验证表单数据 validate")
    private String geetest_validate;
    /**
     * 极验验证二次验证表单数据 seccode
     */
    @NotEmpty(message = "极验验证二次验证表单数据 seccode")
    private String geetest_seccode;
}