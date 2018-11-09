package com.blockeng.rabbit.support;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author maple
 * 可用注解
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import(RabbitConfirmConfig.class)
public @interface EnableRabbitConfirmSupport {
}
