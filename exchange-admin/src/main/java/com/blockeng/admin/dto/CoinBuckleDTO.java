package com.blockeng.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CoinBuckleDTO {

    private Long id;
    private Long userId;
    private String userName;
    private String description;
    private String coinName;
    private BigDecimal amount;
    private int type;
    private int status;
    private Date auditTime;
    private Date created;
    private String auditNameFirst;
    private String auditNameSecond;
    private String reason;
}
