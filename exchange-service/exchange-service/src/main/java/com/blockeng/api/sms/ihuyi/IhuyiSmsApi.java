package com.blockeng.api.sms.ihuyi;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class IhuyiSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setIhuyiConfig(IhuyiConfig ihuyiConfig) {
        IhuyiSmsApi.ihuyiConfig = ihuyiConfig;
    }

    private static IhuyiConfig ihuyiConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("account", ihuyiConfig.getAccount())
                .add("password", ihuyiConfig.getPassword())
                .add("mobile", phone)
                .add("content", text)
                .add("method", "Submit")
                .build();
        Request request = new Request.Builder()
                .url(ihuyiConfig.getUrl())
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
