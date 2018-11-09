package com.clg.wallet.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class ClientBean {
    private Long id;
    private String name;
    private String coinType;
    private BigDecimal creditLimit;
    private String rpcIp;
    private String rpcPort;
    private String rpcUser;
    private String rpcPwd;
    private String rpcIpOut;
    private String rpcPortOut;
    private String rpcUserOut;
    private String rpcPwdOut;
    private String lastBlock;
    private String walletUser;
    private String walletUserOut;
    private String contractAddress;
    private Integer minConfirm;
    private String walletPass;
    private String walletPassOut;
    private String context;
    private String task;
    private Integer status;
}
