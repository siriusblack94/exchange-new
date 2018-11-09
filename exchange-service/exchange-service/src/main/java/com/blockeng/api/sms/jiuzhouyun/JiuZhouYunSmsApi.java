package com.blockeng.api.sms.jiuzhouyun;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class JiuZhouYunSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setJiuZhouYunConfig(JiuZhouYunConfig jiuZhouYunConfig) {
        JiuZhouYunSmsApi.jiuZhouYunConfig = jiuZhouYunConfig;
    }

    private static JiuZhouYunConfig jiuZhouYunConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("account", jiuZhouYunConfig.getAccount())
                .add("password", jiuZhouYunConfig.getPassword())
                .add("mobile", phone)
                .add("content", text)
                .add("action", "send")
                .add("rt", "json")
                .build();
        Request request = new Request.Builder()
                .url(jiuZhouYunConfig.getUrl())
                .post(body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
