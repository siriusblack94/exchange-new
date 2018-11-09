package com.blockeng.api.sms.cl253;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import okhttp3.*;

import java.io.IOException;

/**
 * @author qiang
 */
public class Cl253SmsApi {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static void setCl253Config(Cl253Config cl253Config) {
        Cl253SmsApi.cl253Config = cl253Config;
    }

    private static Cl253Config cl253Config;

    public static String sendTo(String phone, String text) {
        JSONObject json = new JSONObject();
        json.put("account", cl253Config.getAccount());
        json.put("password", cl253Config.getPassword());

        if (Strings.isNullOrEmpty(cl253Config.getApp()) || cl253Config.getApp().equalsIgnoreCase("cl_international_sms")) {
            json.put("mobile", phone);
        } else {
            json.put("phone", phone);
        }
        json.put("msg", text);
        RequestBody body = RequestBody.create(JSON, json.toJSONString());
        Request request = new Request.Builder()
                .url(cl253Config.getUrl())
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