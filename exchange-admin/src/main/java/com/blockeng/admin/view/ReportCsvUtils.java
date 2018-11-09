package com.blockeng.admin.view;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 导出工具类
 * Create Time: 2018年05月18日 18:03
 * C@author lxl
 **/
public class ReportCsvUtils {


    public static void reportList(
            HttpServletResponse response,
            String[] header,
            String[] properties,
            String fileName,
            List<?> soureList
    ) throws Exception {

        if (header == null || properties == null || soureList == null || header.length <= 0 || properties.length <= 0 || soureList.size() <= 0)
            return;
        if (StringUtils.isBlank(fileName)) {
            fileName = "1.csv";
        }
        response.setContentType("application/csv");
        response.setCharacterEncoding("GBK");
        response.setHeader("Content-FileName", URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(header);
        for (Object obj : soureList) {
            csvWriter.write(obj, properties);
        }
        csvWriter.close();


    }


    public static void reportListCsv(
            HttpServletResponse response,
            String[] header,
            String[] properties,
            String fileName,
            List<?> soureList,
            CellProcessor[] PROCESSORS
    ) throws Exception {

        if (header == null || properties == null || soureList == null || header.length <= 0 || properties.length <= 0 || soureList.size() <= 0)
            return;
        if (StringUtils.isBlank(fileName)) {
            fileName = "1.csv";
        }
        response.setContentType("application/csv");
        response.setCharacterEncoding("GBK");
        response.setHeader("Content-FileName", URLEncoder.encode(fileName, "UTF-8"));
        response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");


        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(header);
        for (Object obj : soureList) {
            csvWriter.write(obj, properties, PROCESSORS);
        }
        csvWriter.flush();
        csvWriter.close();
    }

}
