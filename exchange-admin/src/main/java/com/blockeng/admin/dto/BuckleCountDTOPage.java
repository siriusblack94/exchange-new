package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.plugins.Page;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data

public class BuckleCountDTOPage<T> extends Page<T> {

    private BigDecimal totalSum;
    private BigDecimal totalSub;
    private BigDecimal totalAll;
    private String coinName;

    public BuckleCountDTOPage(BigDecimal totalSum, BigDecimal totalSub, BigDecimal totalAll,String coinName) {
        this.totalAll = totalAll;
        this.totalSub = totalSub;
        this.totalSum = totalSum;
        this.coinName = coinName;
    }

    // private Page<BuckleAccountCountDTO> page;
    //  List<BuckleAccountCountDTO> datas;

}
