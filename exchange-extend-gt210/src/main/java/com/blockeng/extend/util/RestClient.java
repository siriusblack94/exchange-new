package com.blockeng.extend.util;

import java.util.Arrays;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * HTTP请求帮助类
 * @author yangzhilong
 *
 */

public class RestClient {

    private static RestTemplate restTemplate;

    /**
     * 注入实现类
     * @param client
     */
    public static void setRestTemplate(RestTemplate client) {
        restTemplate = client;
    }
    /**
     * 无参数或者参数附带在url中
     * @param url
     * @return
     */

    public static String get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        Object response=restTemplate.exchange(url,HttpMethod.GET,entity,Object.class);
        return (String)response;
    }
    /**
     * json格式的post提交
     * @param obj
     * @param url
     * @return
     */
    public static String postJson(String url, Object obj) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String result = null;
        if(obj == null){
            result = "{}";
        }else{
            result = JSON.toJSONString(obj);
        }
        HttpEntity<String> formEntity = new HttpEntity<String>(result,headers);
        return restTemplate.postForObject(url , formEntity, String.class);
    }
    
    /**
     * form格式的post提交
     * @param url
     * @param headerMap
     * @param params
     * @return
     */
    public static String postForm(String url,Map<String , String> headerMap, MultiValueMap<String, String> params){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        for(Map.Entry<String ,String> entry : headerMap.entrySet()){
            headers.add(entry.getKey() , entry.getValue());
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
    }

}