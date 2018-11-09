package com.blockeng.api.sms.m5c;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class M5cSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setM5cConfig(M5cConfig m5cConfig) {
        M5cSmsApi.m5cConfig = m5cConfig;
    }

    private static M5cConfig m5cConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("username", m5cConfig.getUsername())
                .add("password_md5", m5cConfig.getPassword())
                .add("mobile", phone)
                .add("content", text)
                .add("apikey", m5cConfig.getApikey())
                .add("encode", m5cConfig.getEncode())
                .build();
        Request request = new Request.Builder()
                .url(m5cConfig.getUrl())
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
