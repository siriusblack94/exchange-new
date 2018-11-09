package com.blockeng.mining.dto;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class PlantCoinDividendRecordDTO {


    /**
     * 累计分红
     */
    private String rewardDate;

    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;

    private List<PlantCoinDividendTotalDTO> plantCoinDividendDetails;

}
