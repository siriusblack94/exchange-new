package com.blockeng.admin.annotation;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @Auther: sirius
 * @Date: 2018/10/30 14:38
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestIpLimit {
    int count() default Integer.MAX_VALUE;
}
