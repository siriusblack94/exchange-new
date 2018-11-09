package com.blockeng.repository;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "短信发送记录", description = "短信发送记录")
@Document(collection = "send_record")
public class SendRecord implements Serializable {

    /**
     * 主键
     */
    @Id
    @ApiModelProperty(value = "Id", name = "id", example = "1")
    private String id;
    /**
     * 国际电话区号
     */
    @NotEmpty(message = "国际电话区号不能为空")
    @Field(value = "country_code")
    private String countryCode;
    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone", example = "13312979999")
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 短信内容
     */
    @ApiModelProperty(value = "短信内容", name = "content", example = "您的短信验证码为：6606")
    private String content;
    /**
     * 短信模板ID
     */
    @Field(value = "template_code")
    @ApiModelProperty(value = "短信模板ID", name = "templateCode", example = "100001")
    private String templateCode;
    /**
     * 短信模板变量替换JSON串
     */
    @Field(value = "template_param")
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
     * 更新时间
     */
    @Field("last_update_time")
    private Date lastUpdateTime;
    /**
     * 创建时间
     */
    private Date created;
}