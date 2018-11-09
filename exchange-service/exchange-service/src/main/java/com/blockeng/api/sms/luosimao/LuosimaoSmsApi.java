package com.blockeng.api.sms.luosimao;

import okhttp3.*;

import java.io.IOException;

public class LuosimaoSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setLuosimaoConfig(LuosimaoConfig luosimaoConfig) {
        LuosimaoSmsApi.luosimaoConfig = luosimaoConfig;
    }

    private static LuosimaoConfig luosimaoConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("api", luosimaoConfig.getApi())
                .add("mobile", phone)
                .add("message", text)
                .build();
        Request request = new Request.Builder()
                .url(luosimaoConfig.getUrl())
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
