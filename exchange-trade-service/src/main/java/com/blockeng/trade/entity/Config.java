package com.blockeng.trade.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 平台配置信息
 * </p>
 *
 * @author qiang
 * @since 2018-05-12
 */
@Data
@Accessors(chain = true)
public class Config extends Model<Config> {

    private static final long serialVersionUID = 1L;

    /**
     * 平台规则id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
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

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
