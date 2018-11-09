package com.blockeng.admin.enums;

/**
 * @Description: 返回结果代码
 * @Author: Chen Long
 * @Date: Created in 2018/4/8 下午4:01
 * @Modified by: Chen Long
 */
public enum ResultCode {

    SUCCESS(0, "SUCCESS"), USER_NOT_LONG(1000, "用户未登录!"), PARAM_ERROR(10001, "请求参数错误"), MARKET_ERROR(10002, "交易市场错误"), PRICE_ERROR(10003, "委托价格错"), NUM_ERROR(10004, "委托数量错误"), POSITION_DIRECTION_ERROR(10005, "买卖类型错误"), PRICE_TYPE_ERROR(10006, "价格类型错误"), BUSINESS_EXCEPTION(20000, "") // 获取异常信息
    , MOBILE_FORMATERROR(20001, "手机号格式错误"), PASS_FORMAT_ERROR(20002, "密码格式错误"), NAME_FORMAT_ERROR(20003, "姓名不能为空"), UPDATE_ERROR(20004, "数据更新异常"), ERROR_INVITE_ERROR(20005, "错误的邀请码"), MOBILE_EXIST_ERROR(20005, "手机号已注册"), MOBILE_CODE_ERROR(20006, "手机验证码无效"), MOBILE_SMSCODE_ERROR(20007, "验证码错误"), WITH_DRAWALS_ERROR(2008, "提现申请失败"), BANK_NOT_SET(2009, "未设置对应币种的银行卡"), NOT_ALLOW_DRAW(2010, "当前不允许提现!"), USER_NOT_USE(2011, "用户账号被禁用!"), PAY_PASSWORD_ERROR(2012, "交易密码错误！"), USER_NOT_AUTH(2013, "请先实名认证！"), AMOUNT_NOT_ALLOW(2014, "提现金额必须大于0！"), NOT_SET_USDT(2015, "未设置USDT默认价格");

    private int code;

    private String message;

    ResultCode(int code, String message) {
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
