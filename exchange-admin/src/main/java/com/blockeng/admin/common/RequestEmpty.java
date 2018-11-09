package com.blockeng.admin.common;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Objects;

/**
 * 处理请求参数空判断
 *
 * @Author lxl
 * @Date 2018/05/14
 */
public class RequestEmpty {

    public String isRequestParamEmpty(HttpServletRequest request, String... obj) {
        if (null == obj || obj.length > 0) {
            return "";
        }
        for (String ob : obj) {
            String value = request.getParameter(ob);
            if (StringUtils.isBlank(value)) {

                return ob + "不能为空";
            }
        }
        return "";
    }


}
