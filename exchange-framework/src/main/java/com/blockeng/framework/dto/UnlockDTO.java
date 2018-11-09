package com.blockeng.framework.dto;


import com.blockeng.framework.enums.BusinessType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UnlockDTO {

    private Long userId;


    private Long coinId;

    private BigDecimal amount;

    private BusinessType businessType;

    private String desc;

    private Long orderId;
}
