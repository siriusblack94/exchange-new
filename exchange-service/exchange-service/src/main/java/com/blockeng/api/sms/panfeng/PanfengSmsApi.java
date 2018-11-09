package com.blockeng.api.sms.panfeng;

import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class PanfengSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setPanfengConfig(PanfengConfig panfengConfig) {
        PanfengSmsApi.panfengConfig = panfengConfig;
    }

    private static PanfengConfig panfengConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("username", panfengConfig.getUsername())
                .add("passwd", panfengConfig.getPasswd())
                .add("phone", phone)
                .add("msg", text)
                .add("act", "sendmsg")
                .build();
        Request request = new Request.Builder()
                .url(panfengConfig.getUrl())
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