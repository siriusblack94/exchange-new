package com.blockeng.admin.common;

/**
 * Create Time: 2018年05月16日 12:02
 * C@author lxl
 **/
public class CommonUtils {

    /**
     * 查询所有为审核通过和拒绝的 4
     */
    public static final int REVIEWSTATUS_4 = 4;
    /**
     * 待审核0
     */
    public static final int REVIEWSTATUS_0 = 0;
    /**
     * 审核通过1
     */
    public static final int REVIEWSTATUS_1 = 1;
    /**
     * 审核拒绝2
     */
    public static final int REVIEWSTATUS_2 = 2;

    /**
     * 待审核
     */
    public static final int CASH_RECHARGE_STATUS_0 = 0;

    /**
     * 1-审核通过
     */
    public static final int CASH_RECHARGE_STATUS_1 = 1;
    /**
     * 2-拒绝
     */
    public static final int CASH_RECHARGE_STATUS_2 = 2;
    /**
     * 3-充值成功
     */
    public static final int CASH_RECHARGE_STATUS_3 = 3;


    /**
     * 充值审核0拒绝
     */
    public static final int CASH_REVIEWSTATUS_0 = 0;
    /**
     * 充值审核2同意
     */
    public static final int CASH_REVIEWSTATUS_2 = 2;

    /**
     * 用户财产记录财产状态1正常
     */
    public static final int ACCOUNT_STATUS_1 = 1;
    /**
     * 用户财产记录财产状态2冻结
     */
    public static final int ACCOUNT_STATUS_2 = 2;

    /**
     * 提现审核状态 通过1
     */
    public static final int WITHDRAWALS_REVIEWSTATUS_1 = 1;
    /**
     * 提现审核状态 拒绝2
     */
    public static final int WITHDRAWALS_REVIEWSTATUS_2 = 2;

    /**
     * 审核轨迹 审核类型（1提现审核，2提币审核，3实名认证审核，4充值审核）
     */
    public static final String REVIEW_TYPE_1 = "1";
    /**
     * 审核轨迹 审核类型（1提现审核，2提币审核，3实名认证审核，4充值审核）
     */
    public static final String REVIEW_TYPE_2 = "2";
    /**
     * 审核轨迹 审核类型（1提现审核，2提币审核，3实名认证审核，4充值审核）
     */
    public static final String REVIEW_TYPE_3 = "3";
    /**
     * 审核轨迹 审核类型（1提现审核，2提币审核，3实名认证审核，4充值审核）
     */
    public static final String REVIEW_TYPE_4 = "4";
    /**
     * 审核轨迹 审核类型（1提现审核，2提币审核，3实名认证审核，5代理人审核）
     */
    public static final String REVIEW_TYPE_5 = "5";
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    public static final int AUTH_STATUS_0 = 0;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    public static final int AUTH_STATUS_1 = 1;
    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    public static final int AUTH_STATUS_2 = 2;

    /**
     * 1待回復
     */
    public static final int ANSWER_1 = 1;
    /**
     * 2已回復
     */
    public static final int ANSWER_2 = 2;


    /**
     * 1启用
     */
    public static final int STATUS_1 = 1;
    /**
     * 1启用
     */
    public static final int STATUS_0 = 0;

    /**
     * 钱包币
     */
    public static final String QBB = "qbb";
    /**
     * 认购币
     */
    public static final String RGB = "rgb";

    /**
     * 交易对配置信息 1数字货币
     */
    public static final String MARKET_TYPE_1 = "1";
    /**
     * 交易对配置信息 2创新交易
     */
    public static final String MARKET_TYPE_2 = "2";


    /**
     * 1买
     */
    public static final int FORES_STATUS_1 = 1;
    /**
     * 2卖
     */
    public static final int FORES_STATUS_2 = 2;

    /**
     * 用户类型 普通
     */
    public static final int USER_TYPE_1 = 1;
    /**
     * 用户类型 系统
     */
    public static final int USER_TYPE_0 = 0;
    /**
     * 用户类型 代理2
     */
    public static final int USER_TYPE_2 = 2;

    /**
     * 是否作为基础币种标识 0否
     */
    public static final Long BASE_COIN_0 = 0l;
    /**
     * 是否作为基础币种标识 1是
     */
    public static final Long BASE_COIN_1 = 1l;


}
