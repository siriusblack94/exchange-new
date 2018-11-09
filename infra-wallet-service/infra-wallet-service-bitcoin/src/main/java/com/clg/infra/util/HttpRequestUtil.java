package com.clg.infra.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Chen Long
 * @Date: Created in 2018/6/4 上午11:56
 * @Modified by: Chen Long
 */
public class HttpRequestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * Post 请求API
     *
     * @param path
     * @param jsonParam
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T postJson(String path, String jsonParam, Class<T> tClass) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(path);
        CloseableHttpResponse response;
        try {
            if (null != jsonParam) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam, "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }
            response = httpClient.execute(post);
            String strResult = EntityUtils.toString(response.getEntity());
            LOG.info("Request POST " + path + " Param: " + jsonParam);
            LOG.info("Request POST " + path + " Response: " + strResult);
            Gson gson = new Gson();
            return gson.fromJson(strResult, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Post 请求API
     *
     * @param path
     * @param param
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T postRow(String path, String param, Class<T> tClass) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(path);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
        post.setConfig(requestConfig);
        CloseableHttpResponse response;
        try {
            if (StringUtils.isEmpty(param)) {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(param, "UTF-8");
                entity.setContentEncoding("UTF-8");
                post.setEntity(entity);
            }
            response = httpClient.execute(post);
            String strResult = EntityUtils.toString(response.getEntity());
            LOG.info("Request POST " + path + " Param: " + param);
            LOG.info("Request POST " + path + " Response: " + strResult);
            Gson gson = new Gson();
            return gson.fromJson(strResult, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get 请求API
     *
     * @param path   API地址
     * @param tClass 返回数据类型
     * @param <T>
     * @return
     */
    public static <T> T getJson(String path, Class<T> tClass) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(path);
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpget);
            String strResult = EntityUtils.toString(response.getEntity());
            try {
                LOG.info("Request GET " + path + " Response: " + strResult);
                Gson gson = new Gson();
                return gson.fromJson(strResult, tClass);
            } catch (JsonSyntaxException e) {
                //throw new Exception("10000000", "调用接口失败！");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get 请求API
     *
     * @param path API地址
     * @return
     */
    public static String getJson(String path) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(path);
        CloseableHttpResponse response;
        try {
            response = httpClient.execute(httpget);
            String strResult = EntityUtils.toString(response.getEntity());
            return strResult;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * POST 请求API
     *
     * @param path  API地址
     * @param param 请求参数
     * @return
     */
    public static String post(String path, Map<String, String> param) throws Exception {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost post = new HttpPost(path);
            CloseableHttpResponse response;
            post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 装填参数
            List<NameValuePair> postParam = new ArrayList<>();
            if (param != null) {
                for (Map.Entry<String, String> entry : param.entrySet()) {
                    postParam.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            // 设置 post 请求参数
            post.setEntity(new UrlEncodedFormEntity(postParam, "UTF-8"));
            response = httpClient.execute(post);
            String strResult = EntityUtils.toString(response.getEntity());
            LOG.info("接口响应结果：{}", strResult);
            return strResult;
        } catch (Exception e) {
            LOG.error("http请求异常，异常堆栈：{}", e);
            throw new Exception("HttpUtils.post请求异常，异常堆栈信息：{}" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String str = getJson("http://120.78.83.238:7076/trade/market/1");
        System.out.println(str);
    }
}
