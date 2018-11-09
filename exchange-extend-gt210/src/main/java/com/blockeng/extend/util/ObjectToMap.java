package com.blockeng.extend.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: sirius
 * @Date: 2018/11/4 22:39
 * @Description:
 */
public class ObjectToMap {

    public static Map<String, String> objectToMap(Object obj) throws Exception {
                 Map<String, String> map = new HashMap<>();
                 Class<?> clazz = obj.getClass();

                 for (Field field : clazz.getDeclaredFields()) {
                         field.setAccessible(true);
                         String fieldName = field.getName();
                         if ("serialVersionUID".equals(fieldName)) continue;
                         String value = String.valueOf(field.get(obj)) ;
                         map.put(fieldName, value);
                     }
                 return map;
             }
}
