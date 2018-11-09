package com.blockeng.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * <p>
 * 短信
 * </p>
 *
 * @author qiang
 * @since 2018-03-22
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Sms {

    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", example = "13312979999")
    private String phone;
    /**
     * 短信内容
     */
    @ApiModelProperty(value = "短信内容", name = "content", example = "您的短信验证码为：6606")
    private String content;

    /**
     * 短信模板ID
     */
    @ApiModelProperty(value = "短信模板ID", name = "templateCode", example = "100001")
    private String templateCode;

    /**
     * 短信模板变量替换JSON串
     */
    @ApiModelProperty(value = "短信模板变量替换JSON串", name = "templateParam")
    private Map<String, Object> templateParam;
}