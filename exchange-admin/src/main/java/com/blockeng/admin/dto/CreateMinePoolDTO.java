package com.blockeng.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Description: 创建矿池请求参数
 * @Author: Chen Long
 * @Date: Created in 2018/6/29 下午4:20
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateMinePoolDTO {

    /**
     * 名称
     */
    @NotEmpty(message = "矿池名称不能为空")
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 矿池创建者
     */
    @NotNull(message = "矿池创建者不能为空")
    private Long createUser;
}
