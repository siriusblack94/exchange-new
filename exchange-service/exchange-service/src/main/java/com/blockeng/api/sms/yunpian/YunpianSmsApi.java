package com.blockeng.api.sms.yunpian;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class YunpianSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setYunpianConfig(YunpianConfig yunpianConfig) {
        YunpianSmsApi.yunpianConfig = yunpianConfig;
    }

    private static YunpianConfig yunpianConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("apikey", yunpianConfig.getApikey())
                .add("mobile", phone)
                .add("text", text)
                .build();
        Request request = new Request.Builder()
                .url(yunpianConfig.getUrl())
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
