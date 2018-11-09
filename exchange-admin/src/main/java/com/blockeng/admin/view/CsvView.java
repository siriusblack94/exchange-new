package com.blockeng.admin.view;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class CsvView extends AbstractCsvView {
    //导出默认条数 10万
    private static final Integer defaultSize = 100000;

    @Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String fileName = (String) model.get("fileName");
        String[] header = (String[]) model.get("header");
        String[] properties = (String[]) model.get("properties");
        response.setCharacterEncoding("GBK");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "GBK") + "\"");
        response.setHeader("Content-FileName", URLEncoder.encode(fileName, "UTF-8"));
        List<Object> dataSource = (List<Object>) model.get("data");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(header);
        for (Object obj : dataSource) {
            csvWriter.write(obj, properties);
        }
        csvWriter.close();
    }
}
