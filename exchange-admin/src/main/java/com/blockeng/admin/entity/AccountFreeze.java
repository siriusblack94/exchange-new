package com.blockeng.admin.entity;



import com.baomidou.mybatisplus.annotations.TableField;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@Accessors(chain = true)
public class AccountFreeze {
    private Integer userFlag;
    private BigDecimal freeze;
}
