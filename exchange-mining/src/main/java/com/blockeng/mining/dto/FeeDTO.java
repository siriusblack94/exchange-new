package com.blockeng.mining.dto;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel(value = "手续费", description = "手续费")
public class FeeDTO {

    private String name;

    private BigDecimal total;

}
