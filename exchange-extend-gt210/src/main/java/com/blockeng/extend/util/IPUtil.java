package com.blockeng.extend.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: sirius
 * @Date: 2018/11/6 16:02
 * @Description:
 */
public class IPUtil {
    public static  String getIpAddress(HttpServletRequest request) {
        String ip;
        ip = request.getHeader("x-forwarded-for");
        if (isNullIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isNullIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isNullIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isNullIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isNullIp(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        if ("0.0.0.0.0.0.0.1".equals(ip) || "0.0.0.0.0.0.0.1%0".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    private static boolean isNullIp(final String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
