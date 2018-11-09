package com.blockeng.api.sms.smsbao;

import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author qiang
 */
public class SmsBaoSmsApi {

    public static OkHttpClient client = new OkHttpClient();

    public static void setSmsBaoConfig(SmsBaoConfig smsBaoConfig) {
        SmsBaoSmsApi.smsBaoConfig = smsBaoConfig;
    }

    private static SmsBaoConfig smsBaoConfig;

    public static String sendTo(String phone, String text) {
        RequestBody body = null;
        try {
            body = new FormBody.Builder()
                    .add("u", smsBaoConfig.getU())
                    .add("p", smsBaoConfig.getP())
                    .add("m", phone)
                    .add("c", URLEncoder.encode(text, "UTF-8"))
                    .build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder()
                .url(smsBaoConfig.getUrl())
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
