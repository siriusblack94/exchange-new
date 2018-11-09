package com.blockeng.mining.dto;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class DividendRecordDTO {


    /**
     * 未解冻
     */
    private BigDecimal inviteAmount;

    /**
     * 解冻日期
     */
    private String rewardDate;

    private BigDecimal usdtAccount;

    private BigDecimal cnyAccount;
}
