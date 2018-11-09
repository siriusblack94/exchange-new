package com.blockeng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

/**
 * @author qiang
 */
@Configuration
public class I18nConfig {

    /**
     * cookie区域解析器
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        //设置默认区域,
        localeResolver.setDefaultLocale(Locale.CHINA);
        localeResolver.setCookieName("lang");
        localeResolver.setCookieMaxAge(3600);//设置cookie有效期.
        return localeResolver;
    }
}