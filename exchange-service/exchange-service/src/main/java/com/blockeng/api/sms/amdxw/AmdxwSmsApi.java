package com.blockeng.api.sms.amdxw;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class AmdxwSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setAmdxwConfig(AmdxwConfig amdxwConfig) {
        AmdxwSmsApi.amdxwConfig = amdxwConfig;
    }

    private static AmdxwConfig amdxwConfig;

    /**
     * 发送接口
     *
     * @param phone
     * @param text
     * @return
     * @throws IOException
     */
    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("userid", amdxwConfig.getUserid())
                .add("account", amdxwConfig.getAccount())
                .add("password", amdxwConfig.getPassword())
                .add("mobile", phone)
                .add("content", text)
                .add("action", "send")
                .build();
        Request request = new Request.Builder()
                .url(amdxwConfig.getUrl())
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

    /**
     * 获取短信数量接口
     *
     * @return
     */
    public static String querySendDetails() {
        RequestBody body = new FormBody.Builder()
                .add("userid", amdxwConfig.getUserid())
                .add("account", amdxwConfig.getAccount())
                .add("password", amdxwConfig.getPassword())
                .add("action", "overage")
                .build();
        Request request = new Request.Builder()
                .url(amdxwConfig.getUrl())
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