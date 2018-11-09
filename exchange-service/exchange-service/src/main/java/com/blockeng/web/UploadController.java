package com.blockeng.web;

import cn.javaer.aliyun.spring.boot.autoconfigure.oss.AliyunOssProperties;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.blockeng.config.AliyunOssAdvancedProperties;
import com.blockeng.framework.http.Response;
import com.google.common.base.Splitter;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qiang
 */
@RestController
@RequestMapping
@Api(value = "文件上传", description = "文件上传 REST API")
@EnableConfigurationProperties(AliyunOssAdvancedProperties.class)
public class UploadController {

    @Autowired
    private AliyunOssProperties aliyunOssProperties;

    @Autowired
    private AliyunOssAdvancedProperties aliyunOssAdvancedProperties;

    @Autowired
    private OSSClient ossClient;

    public String getHost() {
        return aliyunOssAdvancedProperties.getProtocol() + "://" + aliyunOssProperties.getEndpoint();
    }

    public String getLoadUrl() {
        return aliyunOssAdvancedProperties.getProtocol() + "://" + aliyunOssAdvancedProperties.getLoadUrl();
    }

    @GetMapping("/preupload")
    Object preupload() {
        String accessId = aliyunOssProperties.getAccessKeyId();
        String host = getHost();
        String dir = "static/";
        long expireTime = 30;
        long expireEndTime = System.currentTimeMillis() + expireTime * 10000;
        Date expiration = new Date(expireEndTime);
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

        String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
        byte[] binaryData = new byte[0];
        try {
            binaryData = postPolicy.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        String postSignature = ossClient.calculatePostSignature(postPolicy);

        Map<String, String> respMap = new LinkedHashMap<String, String>();
        respMap.put("accessid", accessId);
        respMap.put("policy", encodedPolicy);
        respMap.put("signature", postSignature);
        respMap.put("dir", dir);
        respMap.put("host", host);
     //   respMap.put("loadUrl",getLoadUrl());

        JSONObject callback = new JSONObject();
        callback.put("callbackUrl", aliyunOssAdvancedProperties.getCallbackUrl());
        callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.put("callbackBodyType", "application/x-www-form-urlencoded");

        respMap.put("callback", Base64.encodeBase64String(callback.toJSONString().getBytes()));
        respMap.put("expire", String.valueOf(expireEndTime / 1000));
        return Response.ok(respMap);
    }

    @PostMapping("/upload/callback")
    Object callback(@RequestHeader HttpHeaders headers, @RequestBody String payload) {
        JSONObject result = new JSONObject();
        Map<String, String> params = Splitter.on("&").withKeyValueSeparator("=").split(payload);
        String filename = null;
        try {
            filename = URLDecoder.decode(params.get("filename"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String host = getLoadUrl() + "/" + filename;
        result.put("uri", host);
        result.put("Status", "OK");
        return result;
    }
}
