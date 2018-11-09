package com.blockeng.task.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * 平台配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "config")
public class Config {

    @Id
    private String objectId;

    /**
     * 平台规则id
     */
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
    private String desc;

    /**
     * 配置值
     */
    private String value;
}
