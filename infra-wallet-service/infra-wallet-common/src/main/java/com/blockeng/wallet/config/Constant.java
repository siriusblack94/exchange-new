package com.blockeng.wallet.config;

public interface Constant {
    int TASK_OPEN = 1;//任务开启为0

    String ALL_JOB = "all_job";//所有任务开启标志

    int INSERT_SUCCESS = 1; //充值成功


    int ADMIN_ADDRESS_TYPE_COLLECT = 1; //归账

    int ADMIN_ADDRESS_TYPE_PAY = 2; //打款

    int ADMIN_ADDRESS_TYPE_FEE = 3; //手续费

    int ADMIN_ADDRESS_TYPE_RECHARGE = 4; //充值账户

    int PAY_SUCCESS_STATUS = 5;

    int PAY_FAILED = 6; //余额不足

    int PAY_TEMP = 7; //打币中

    int PAY_FAILED_REJECT = 2; //提币,拒绝

    String PAY_SUCCESS_INFO = "打款成功";

    String PAY_NOT_ENOUGH_INFO = "余额不足";

    String PAY_ACCOUNT_NOT_NULL_INFO = "打款账户不能为空";

    String PAY_ACCOUNT_PRI_INFO = "打款私钥不能为空";

    String PAY_FAILED_INFO = "打款失败";

    String RECHARGE_ACCOUNT_NOT_NULL = "充值主账户不能为空";


    String FEATURES_RECHARGE = "recharge";

    String FEATURES_COLLECT = "collect";


    String FEATURES_DRAW = "draw";

    String FEATURES_OTHER = "other";

    String FEATURES_ADDRESS = "address";


    String NEO_CONTRACT = "c56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b";


    String MOCK_TXID = "0000000000000000000000000000000000000000000";


}
