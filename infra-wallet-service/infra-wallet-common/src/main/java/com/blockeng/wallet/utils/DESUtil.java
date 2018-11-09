package com.blockeng.wallet.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by kuangxiaoguo on 16/9/11.
 * <p>
 * DES加密工具类
 */
@Component
public class DESUtil {

    @Autowired
    private ReadProperties readProperties;

    /**
     * 加密
     *
     * @return 返回解密内容
     */
    public String decrypt(String data) {
        return DecryptCoder.decrypt(data, readProperties.aesKey);
    }

    /**
     * 解密
     *
     * @return 返回加密内容
     */
    public String encrypt(String data) {
        return EncryptCoder.encrypt(data, readProperties.aesKey);
    }


    public static void main(String[] args) {

        String ltcdecrypt = DecryptCoder.decrypt("40A1E189FE32A97C9366B08ABFC7AD842617D44A987ADBB78EB9D9D9FC182AA760BE016AABF6DF17", "Rmvh66e5cqA72mkx");
        System.out.println(ltcdecrypt);
//        ltcdecrypt = EncryptCoder.encrypt("dsasdasdmkmklswqekk43kldsd2", "!y@l#d$p%l^a&n*t");
//        System.out.println(ltcdecrypt);
//
//        ltcdecrypt = EncryptCoder.encrypt("sarasdf2443234dfggd67576sdassd1223", "!y@l#d$p%l^a&n*t");
//        System.out.println(ltcdecrypt);


    }
}

//dsasdasdmkmklswqekk43kldsd2
//sarasdf2443234dfggd67576sdassd1223