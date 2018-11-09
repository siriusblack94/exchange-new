package com.blockeng.admin.annotation;

import com.blockeng.admin.enums.SysLogTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志类型
     *
     * @return
     */
    String value() default "";

    /**
     * 描述
     *
     * @return
     */
    String description() default "";

    /**
     * type
     *
     * @return
     */
    SysLogTypeEnum type() default SysLogTypeEnum.NONE;

}