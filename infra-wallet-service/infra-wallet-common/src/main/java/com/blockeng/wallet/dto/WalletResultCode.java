package com.blockeng.wallet.dto;

/**
 * @Description: 返回结果代码
 * @Author: Chen Long
 * @Date: Created in 2018/4/8 下午4:01
 * @Modified by: Chen Long
 */
public enum WalletResultCode {

    SUCCESS(1, "SUCCESS"), PARAM_ERROR(10001, "请求参数错误"), UPDATE_WALLET_ERROR(1002, "更新钱包密码失败"), PAY_ERROR_NOT_ENOUGH(1003, "钱包余额不足"), UNKNOWN_ERROR(1004, "未知异常"), WITH_DRAW_SUCCESS(1005, "提币成功"), WITH_DRAW_FAILED(1006, "提币失败"), GET_ADDRESS_FAILED(1007, "获取用户地址失败"), NOT_FIND_WALLET_ADDRESS(10009, "未发现该钱包地址"), NOT_EXIST_COIN_ID(10010, "不存在改名称币种"), USER_NOT_LONG(100011, "参数错误");

    private int code;

    private String message;

    WalletResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
