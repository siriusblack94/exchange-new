package com.blockeng.extend.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * Created by kuangxiaoguo on 16/9/11.
 * <p>
 * DES加密工具类
 */
@Component
public class DESUtil {

    @Value("${plant.aes.key}")
    public String aesKey;//加密key

    /**
     * 加密
     *
     * @return 返回解密内容
     */
    public String decrypt(String data) {
        if (StringUtils.isBlank(data)) return null;
        return DecryptCoder.decrypt(data, aesKey);
    }

    /**
     * 解密
     *
     * @return 返回加密内容
     */
    public String encrypt(String data) {
        if (StringUtils.isBlank(data)) return null;
        return EncryptCoder.encrypt(data, aesKey);
    }


    public static void main(String[] args) {
        String ltcdecrypt = EncryptCoder.encrypt("0R6lqMPALnvF2T5x9euObUscmH", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
          ltcdecrypt = EncryptCoder.encrypt("v0p5HieCIgZsTkOjLmKSFVw8Q1", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
          ltcdecrypt = EncryptCoder.encrypt("ujWBtpdETCKHPa4MJNibOnZ9kR", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
          ltcdecrypt = EncryptCoder.encrypt("nIYDbXgfJAtMzCrT3j2Ew6xq9V", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
          ltcdecrypt = EncryptCoder.encrypt("PRb8wOKi5zlCa7WrhX3yxYtj2N", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
//        ltcdecrypt = DecryptCoder.decrypt(ltcdecrypt, "!h@w#c$w%x^t&a*(");
//        System.out.println(ltcdecrypt);
//
//        ltcdecrypt = EncryptCoder.encrypt("ssHpDCSyW4K4zX5tdmapuERarAX6B", "!h@w#c$w%x^t&a*(");
//        System.out.println(ltcdecrypt);
//        ltcdecrypt = DecryptCoder.decrypt(ltcdecrypt, "!h@w#c$w%x^t&a*(");
//        System.out.println(ltcdecrypt);


    }
}

