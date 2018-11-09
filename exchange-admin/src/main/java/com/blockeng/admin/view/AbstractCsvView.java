package com.blockeng.admin.view;

import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Descrition:
 * @Author: Chen Long
 * @Date: Created in 2018/2/10 上午12:49
 * @Modified by:
 */
public abstract class AbstractCsvView extends AbstractView {

    private static final String CONTENT_TYPE = "application/csv";

    private String url;

    public AbstractCsvView() {
        setContentType(CONTENT_TYPE);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected final void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(getContentType());
        buildCsvDocument(model, request, response);
    }

    protected abstract void buildCsvDocument(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
