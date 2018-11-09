package com.blockeng.wallet.config;


import com.clg.wallet.bean.ClientBean;
import com.clg.wallet.enums.CoinType;
import com.blockeng.wallet.help.ClientInfo;
import com.blockeng.wallet.utils.ReadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckCoinStatus {


    @Autowired
    private ReadProperties readProperties;

    public boolean rechargeIsRunning() {
        return readProperties.recharge == Constant.TASK_OPEN;
    }

    public boolean collectIsRunning() {
        return readProperties.collect == Constant.TASK_OPEN;
    }

    public boolean drawIsRunning() {
        return readProperties.draw == Constant.TASK_OPEN;
    }

    public boolean otherIsRunning() {
        return readProperties.other == Constant.TASK_OPEN;
    }


    private boolean addressIsRunning() {
        return readProperties.address == Constant.TASK_OPEN;
    }


    public boolean allIsRunning() {
        return readProperties.allJob == Constant.TASK_OPEN;
    }


    public boolean featuresIsOpen(String feature) {
        if (feature.startsWith(Constant.FEATURES_COLLECT)) {
            return collectIsRunning();
        } else if (feature.startsWith(Constant.FEATURES_DRAW)) {
            return drawIsRunning();
        } else if (feature.startsWith(Constant.FEATURES_OTHER)) {
            return otherIsRunning();
        } else if (feature.startsWith(Constant.FEATURES_RECHARGE)) {
            return rechargeIsRunning();
        } else if (feature.startsWith(Constant.FEATURES_ADDRESS)) {
            return addressIsRunning();
        }
        return false;
    }


}
