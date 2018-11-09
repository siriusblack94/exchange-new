package com.blockeng.sharding;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtil {

    //起始时间
    public static LocalDate BEGIN_DATE = LocalDate.now();

    public static String TABLE_NAME_FORMAT = "yyyy-MM-dd";

    public static void setBEGIN_DATE(LocalDate date) {
        BEGIN_DATE = date;
    }

    public static void setTABLE_NAME_FORMAT(String format) {
        TABLE_NAME_FORMAT = format;
    }

    //两个日期间隔多少天
    public static long until(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    public static LocalDate toLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(String date, String format) {
        //   LocalDateTime time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
        //    if(time.getHour()==0&&time.getMinute()==0&&time.getSecond()==0){
        //       return time.toLocalDate();
        //   }
        //    return time.toLocalDate().plusDays(1);
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
    }

    // public static LocalDateTime toLocalDateTime(String date, String format) {
    //    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
    // }

    public static String toDateString(LocalDate localDate, String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

}
