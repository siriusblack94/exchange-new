package com.blockeng.sharding;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author maple
 * @date 2018/4/27 12:56
 */
public class ConfigUtil {


    /**
     * 将注解属性赋值给对象当中，属性必须一致
     */
    public static void putAnnotationToBean(Object obj, Annotation annotation) {

        ReflectionUtils.doWithFields(obj.getClass(), field -> {
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, obj, ifClassToString(AnnotationUtils.getValue(annotation, field.getName())));
                }
        );
    }

    /**
     * 将prperties的map接收器，转换成对象，属性名一致
     */
    public static Map propertiesToMap(Map<String, String> prop, Class cls) {
        try {
            Map<String, List<String>> items = prop.keySet().stream()
                    .collect(Collectors.groupingBy(c -> c.toString().split("\\.")[0]));
            Map result = new HashMap();
            items.forEach((k, v) -> {
                final Object target;
                try {
                    target = cls.getConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                v.forEach(s -> {
                    String[] ss = s.split("\\.");
                    String fieldName = ss[ss.length - 1];
                    Field field = ReflectionUtils.findField(cls, fieldName);
                    ReflectionUtils.makeAccessible(field);
                    ReflectionUtils.setField(field, target, prop.get(s));
                    result.put(k, target);
                });
            });
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author maple
     * @date 2018/4/26 21:55
     * 那五个handler注解，有class类型的属性，转换成string类名
     */
    public static Object ifClassToString(Object o) {
        if (o instanceof Class) {
            if (o == void.class) {
                return "";
            }
            return ((Class) o).getName();
        }
        return o;
    }
}
