package com.blockeng.extend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class ReturnInfo {

    /**
     * 状态码
     */
    private String status;

    /**
     * 备注
     */
    private String message;

    /**
     * 登陆外部系统的token
     * 值
     */
    private String token;

}
