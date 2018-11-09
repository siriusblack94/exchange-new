package com.blockeng.admin.view;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @Descrition:
 * @Author: Chen Long
 * @Date: Created in 2018/2/10 上午1:02
 * @Modified by:
 */
public class CsvViewResolver implements ViewResolver {

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        CsvView view = new CsvView();
        return view;
    }
}
