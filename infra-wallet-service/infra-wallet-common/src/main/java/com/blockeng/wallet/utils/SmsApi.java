package com.blockeng.wallet.utils;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author qiang
 */
@Component
public class SmsApi {

    public static OkHttpClient client = new OkHttpClient();

    @Value("${sms.url}")
    public void setUrl(String url) {
        SmsApi.url = url;
    }

    @Value("${sms.userid}")
    public void setUserid(String userid) {
        SmsApi.userid = userid;
    }

    @Value("${sms.account}")
    public void setAccount(String account) {
        SmsApi.account = account;
    }

    @Value("${sms.password}")
    public void setPassword(String password) {
        SmsApi.password = password;
    }

    private static String url;

    private static String userid;

    private static String account;

    private static String password;

    /**
     * 发送接口
     *
     * @param phone
     * @param text
     * @return
     * @throws IOException
     */
    public static String sendTo(String phone, String text) {
        RequestBody body = new FormBody.Builder()
                .add("userid", userid)
                .add("account", account)
                .add("password", password)
                .add("mobile", phone)
                .add("content", text)
                .add("action", "send")
                .build();
        Request request = new Request.Builder()
                .url(url)
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

    /**
     * 获取短信数量接口
     *
     * @return
     */
    public static String querySendDetails() {
        RequestBody body = new FormBody.Builder()
                .add("userid", userid)
                .add("account", account)
                .add("password", password)
                .add("action", "overage")
                .build();
        Request request = new Request.Builder()
                .url(url)
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