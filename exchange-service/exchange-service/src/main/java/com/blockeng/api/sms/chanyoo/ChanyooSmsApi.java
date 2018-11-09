package com.blockeng.api.sms.chanyoo;

import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author qiang
 */
public class ChanyooSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setChanyooConfig(ChanyooConfig chanyooConfig) {
        ChanyooSmsApi.chanyooConfig = chanyooConfig;
    }

    private static ChanyooConfig chanyooConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = null;
        try {
            body = new FormBody.Builder()
                    .add("username", chanyooConfig.getUsername())
                    .add("password", chanyooConfig.getPassword())
                    .add("receiver", phone)
                    .add("content", URLEncoder.encode(text, "utf-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(chanyooConfig.getUrl())
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
