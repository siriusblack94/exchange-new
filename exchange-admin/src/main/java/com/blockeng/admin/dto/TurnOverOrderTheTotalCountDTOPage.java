package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.plugins.Page;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TurnOverOrderTheTotalCountDTOPage<T> extends Page<T> {

    private BigDecimal totalTurnOverNumber;
    private BigDecimal totalTurnOverAmount;
    private BigDecimal totalBuyFee;
    private BigDecimal totalSellFee;
    private Integer totalOrderNum;
//    @TableField(exist = false)
//    private Page<TurnOverOrderTheCountDTO> page;
}
