package com.blockeng.mining.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class PoolUserHoldCoinDTO {
    private String mobile;
    private String email;
    private String userName;
    private BigDecimal amount;
}
