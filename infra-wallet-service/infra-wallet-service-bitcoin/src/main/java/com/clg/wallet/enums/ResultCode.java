package com.clg.wallet.enums;

public enum ResultCode {
    SUCCESS(0, "SUCCESS"),
    NOT_ENOUGH(1000, "余额不足"),
    TYPE_ERROR(1001, "类型错误"),
    TYPE_MP5_ERROR(1002, "不是MP5类型"),
    MP5_TX_ERROR(1003, "转账交易失败"),
    OTHER_ERROR(1004, "未知异常"),
    SERVER_ERROR(1005, "服务器连接异常"),
    EXPLORER_ERROR(1006, "获取浏览器区块高度异常"),
    CREATE_CREDENTIALS_ERROR(1007, "创建Credentials失败"),
    CREATE_CONTRACT_TIME_OUT(1008, "创建合约超时"),
    PASS_ERROR(1009, "资金密码错误"),
    FEE_NOTENOUGH_ERROR(1010, "当前钱包无充足手续费"),
    TX_BLOCKING(1011, "当前取款正在打包中..."),
    TX_FAILED(1012, "交易失败...");

    private int code;
    private String message;

    private ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
