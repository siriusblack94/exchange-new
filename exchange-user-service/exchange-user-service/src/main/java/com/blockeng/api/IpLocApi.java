package com.blockeng.api;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.config.IpLocProperties;
import com.blockeng.entity.IpInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author qiang
 */
@Component
@EnableConfigurationProperties(IpLocProperties.class)
public class IpLocApi {

    public static OkHttpClient client = new OkHttpClient();

    @Autowired
    public void setIpLocProperties(IpLocProperties ipLocProperties) {
        IpLocApi.ipLocProperties = ipLocProperties;
    }

    private static IpLocProperties ipLocProperties;

    /**
     * IP地址查询
     *
     * @param ip
     * @return
     * @throws IOException
     */
    public static IpInfo getIpInfo(String ip) {
        Request request = new Request.Builder()
                .url("https://api01.aliyun.venuscn.com/ip?ip=" + ip)
                .header("Authorization", "APPCODE " + ipLocProperties.getAppcode())
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JSONObject responseObject = JSONObject.parseObject(response.body().string());
                if (responseObject.getIntValue("ret") == 200) {
                    return responseObject.getJSONObject("data").toJavaObject(IpInfo.class);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
