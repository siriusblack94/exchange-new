package com.blockeng.mining.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class PoolDetailItemDTO {

    private String userName;

    private BigDecimal amount;

    private Long userId;


}
