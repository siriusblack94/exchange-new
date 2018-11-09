package com.blockeng.admin.common;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

/**
 * Create Time: 2018年06月07日 15:23
 *
 * @author lxl
 * @Dec
 **/
public class MD5Utils {

    public static String getMD5(String password) {
        try {
            if (StringUtils.isEmpty(password)) {
                return null;
            }
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
            // 标准的md5加密后的结果
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
