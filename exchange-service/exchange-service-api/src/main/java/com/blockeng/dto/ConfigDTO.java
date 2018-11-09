package com.blockeng.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ConfigDTO {

    /**
     * 平台规则id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 配置规则代码
     */
    private String type;

    /**
     * 配置规则代码
     */
    private String code;

    /**
     * 配置规则名称
     */
    private String name;

    /**
     * 配置规则描述
     */
    private String description;

    /**
     * 配置值
     */
    private String value;
}
