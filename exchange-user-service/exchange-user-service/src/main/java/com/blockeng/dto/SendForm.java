package com.blockeng.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author qiang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendForm implements Serializable {

    /**
     * 国际电话区号
     */
    private String countryCode;
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

    private String mobile;

    private String email;
}
