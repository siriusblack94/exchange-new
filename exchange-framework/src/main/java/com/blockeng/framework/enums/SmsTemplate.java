package com.blockeng.framework.enums;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/5/22 下午6:38
 * @Modified by: Chen Long
 */
public enum SmsTemplate {
    COIN_TRANSFER("COIN_TRANSFER","站内转帐"),
    EXTEND_POINT_REDEMPTION("EXTEND_POINT_REDEMPTION", "积分兑换"),
    SIGN("SIGN", "短信签名"), REGISTER_VERIFY("REGISTER_VERIFY", "注册短信模板"), FORGOT_VERIFY("FORGOT_VERIFY", "找回密码短信模板"), REGISTER_AGENT("REGISTER_AGENT", "代理人注册短信模板"), UNDER_LINE_REFUSE("UNDER_LINE_REFUSE", "法币充值拒绝短信模板"), UNDER_LINE_SUCCESS("UNDER_LINE_SUCCESS", "法币充值成功短信模板"), CASH_WITHDRAW_REFUSE("CASH_WITHDRAW_REFUSE", "法币提现审核拒绝短信模板"), WITHDRAW_APPLY("WITHDRAW_APPLY", "提币申请短信模板"), WITHDRAW_SUCCESS("WITHDRAW_SUCCESS", "提币成功短信模板"), CHANGE_PHONE_VERIFY("CHANGE_PHONE_VERIFY", "修改手机号码短信模板"), CHANGE_LOGIN_PWD_VERIFY("CHANGE_LOGIN_PWD_VERIFY", "修改登录密码短信模板"), VERIFY_OLD_PHONE("VERIFY_OLD_PHONE", "验证老账户的验证码"), CHANGE_PAY_PWD_VERIFY("CHANGE_PAY_PWD_VERIFY", "修改资金密码短信模板"), CASH_WITHDRAWS("CASH_WITHDRAWS", "提现申请模板"), FORGOT_PAY_PWD_VERIFY("FORGOT_PAY_PWD_VERIFY", "找回资金密码短信模板"), API_KEY_GET_VERIFY("API_KEY_GET_VERIFY", "获取API KEY短信验证模板"), LOGIN("LOGIN", "登录模板"), COIN_RECHARGE_SUCCESS("COIN_RECHARGE_SUCCESS", "充币成功模板");

    private String code;
    private String desc;

    SmsTemplate(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
