package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BuckleAccountCountDTO {



    private Long userId;

    private BigDecimal total;

    private BigDecimal sumTotal;

    private BigDecimal subTotal;

    private String coinName;

    public BuckleAccountCountDTO(Long userId, BigDecimal total, BigDecimal sumTotal, BigDecimal subTotal) {
        this.userId = userId;
        this.total = total;
        this.sumTotal = sumTotal;
        this.subTotal = subTotal;
    }
}
