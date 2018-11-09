package com.blockeng.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 用户短信记录
 * </p>
 *
 * @author qiang
 * @since 2018-03-22
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "短信内容", description = "短信内容")
public class SmsDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "Id", name = "id", example = "1")
    private Long id;
    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "mobile", example = "13312979999")
    private String mobile;

    private String email;
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
    /**
     * 状态
     * 0-失败；
     * 1-成功
     */
    @ApiModelProperty(value = "状态", name = "status", example = "1")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    /**
     * 短信发送时间
     */
    @ApiModelProperty(value = "短信发送时间", name = "created")
    private Date created;
}