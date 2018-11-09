package com.blockeng.framework.enums;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/19 下午5:44
 * @Modified by: Chen Long
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MergeDepthDTO {

    /**
     * 合并类型
     */
    private String mergeType;

    /**
     * 合并精度
     */
    private BigDecimal value;
}
