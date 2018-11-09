package com.blockeng.api.sms.emay;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class EmaySmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setEmayConfig(EmayConfig emayConfig) {
        EmaySmsApi.emayConfig = emayConfig;
    }

    private static EmayConfig emayConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("appId", emayConfig.getAppId())
                .add("gzip", emayConfig.getGzip())
                .add("encode", emayConfig.getEncode())
                .add("mobile", phone)
                .add("content", text)
                .build();
        Request request = new Request.Builder()
                .url(emayConfig.getUrl())
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
