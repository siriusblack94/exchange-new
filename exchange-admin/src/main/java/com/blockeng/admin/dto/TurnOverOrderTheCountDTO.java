package com.blockeng.admin.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TurnOverOrderTheCountDTO {

    String userId;
    String marketName;
    BigDecimal turnOverNumber;
    BigDecimal turnOverAmount;
    BigDecimal buyFee;
    BigDecimal sellFee;
    Integer orderNum;
    Integer userType;
    @TableField(exist = false)
    String userTypeStr;
}
