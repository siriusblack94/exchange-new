package com.blockeng.wallet.help;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.wallet.config.Constant;
import com.blockeng.wallet.utils.ReadProperties;
import com.blockeng.wallet.utils.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IpUtils {


    @Autowired
    private ReadProperties readProperties;

    String netIp = "";

    public String getNetIp() {
        if (!StringUtils.isEmpty(netIp)) {
            return netIp;
        }
        if (readProperties.localLimit == Constant.TASK_OPEN &&
                StringUtils.isEmpty(netIp) &&
                !StringUtils.isEmpty(readProperties.ipUrl)) { //是否开启ip限制
            JSONObject json = HttpRequestUtil.getJson(readProperties.ipUrl, JSONObject.class);
            if (null != json) {
                netIp = json.getString("query");
            } else {
                json = HttpRequestUtil.getJson("https://ipapi.co/json", JSONObject.class);
                if (null != json) {
                    netIp = json.getString("ip");
                }
            }
        }
        return netIp;
    }


    public boolean isIP(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";

        Pattern pat = Pattern.compile(rexp);

        Matcher mat = pat.matcher(addr);

        boolean ipAddress = mat.find();

        return ipAddress;
    }
}
