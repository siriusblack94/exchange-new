package com.blockeng.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/7/1 上午1:40
 * @Modified by: Chen Long
 */
@Data
@Accessors(chain = true)
public class GetAddressDTO {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 币种ID
     */
    private Long coinId;

    /**
     * 币种名称
     */
    private String coinName;

    public GetAddressDTO(Long userId, Long coinId) {
        this.userId = userId;
        this.coinId = coinId;
    }
}
