package com.blockeng.api;

import com.alibaba.fastjson.JSONObject;
import com.blockeng.config.IdCardProperties;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qiang
 */
@Component
@EnableConfigurationProperties(IdCardProperties.class)
public class IdCardApi {

    public static OkHttpClient client = new OkHttpClient();

    @Autowired
    public void setIdCardProperties(IdCardProperties idCardProperties) {
        IdCardApi.idCardProperties = idCardProperties;
    }

    private static IdCardProperties idCardProperties;

    /**
     * 身份证验证
     *
     * @param cardNo
     * @param realName
     * @return
     * @throws IOException
     */
    public static boolean verify(String realName, String cardNo) throws IOException {
        Request request = new Request.Builder()
                .url("http://aliyunverifyidcard.haoservice.com/idcard/VerifyIdcardv2?realName=" + realName + "&cardNo=" + cardNo)
                .header("Authorization", "APPCODE " + idCardProperties.getAppcode())
                .get()
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            JSONObject responseObject = JSONObject.parseObject(response.body().string());
            if (responseObject.getIntValue("error_code") == 0) {
                return responseObject.getJSONObject("result").getBooleanValue("isok");
            }
        }
        return false;
    }

}
