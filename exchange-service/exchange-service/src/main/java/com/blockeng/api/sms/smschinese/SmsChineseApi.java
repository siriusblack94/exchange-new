package com.blockeng.api.sms.smschinese;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class SmsChineseApi {


    public static OkHttpClient client = new OkHttpClient();

    public static void setSmsChineseConfig(SmsChineseConfig smsChineseConfig) {
        SmsChineseApi.smsChineseConfig = smsChineseConfig;
    }

    private static SmsChineseConfig smsChineseConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("Uid", smsChineseConfig.getUid())
                .add("Key", smsChineseConfig.getKey())
                .add("smsMob", phone)
                .add("smsText", text)
                .build();
        Request request = new Request.Builder()
                .url(smsChineseConfig.getUrl())
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
