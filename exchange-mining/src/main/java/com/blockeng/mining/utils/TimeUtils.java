package com.blockeng.mining.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;


public class TimeUtils {

    /**
     * 获取当天日期的,返回日期字符串
     *
     * @return
     */
    public static String getNowDay() {
        return LocalDate.now().toString();
    }


    public static String getYesterdayDate() {
        return LocalDate.now().minusDays(1).toString();
    }

    public static String get2DdayBeforeDate() {
        return LocalDate.now().minusDays(2).toString();
    }


    public static String getYesterdayDate(String nowDay) {
        LocalDate local;
        if (!StringUtils.isEmpty(nowDay)) {
            local = LocalDate.parse(nowDay);
        } else {
            local = LocalDate.now();
        }
        return local.minusDays(1).toString();
    }

    /**
     * 当天是不是礼拜一
     *
     * @return
     */
    public static boolean isWeekFirstDay(String nowDay) {
        String weekFirst = getWeekFirst(nowDay);
        return nowDay.equals(weekFirst);
    }


    /**
     * 获取礼拜一
     *
     * @return
     */
    public static String getWeekFirst(String nowDay) {
        LocalDate local;
        if (!StringUtils.isEmpty(nowDay)) {
            local = LocalDate.parse(nowDay);
        } else {
            local = LocalDate.now();
        }
        DayOfWeek dayOfWeek = local.getDayOfWeek();//获取今天是周几
        LocalDate firstWeek = local.minusDays(dayOfWeek.getValue() - 1);//算出上周一
        return firstWeek.toString();
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getMonethFirst(String nowDay) {
        LocalDate today;
        if (!StringUtils.isEmpty(nowDay)) {
            today = LocalDate.parse(nowDay);
        } else {
            today = LocalDate.now();
        }

        //本月的第一天
        return today.with(TemporalAdjusters.firstDayOfMonth()).toString();
    }



    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getMonethLast(String nowDay) {
        LocalDate today;
        if (!StringUtils.isEmpty(nowDay)) {
            today = LocalDate.parse(nowDay);
        } else {
            today = LocalDate.now();
        }

        //本月的第一天
        return today.with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    /**
     * 获取上月第一天
     *
     * @return
     */
    public static String getPriMonethFirst(String nowDay) {
        LocalDate today;
        if (!StringUtils.isEmpty(nowDay)) {
            today = LocalDate.parse(nowDay);
        } else {
            today = LocalDate.now();
        }

        //本月的第一天
        return today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).toString();
    }

    /**
     * 获取上月最后一天
     *
     * @return
     */
    public static String getPriMonethLast(String nowDay) {
        LocalDate today;
        if (!StringUtils.isEmpty(nowDay)) {
            today = LocalDate.parse(nowDay);
        } else {
            today = LocalDate.now();
        }

        //本月的第一天
        return today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).toString();
    }

    /**
     * 当天是不是一号
     *
     * @return
     */
    public static boolean isMonethOneDay(String nowDay) {
        String monethFirst = getMonethFirst(nowDay);
        return nowDay.equals(monethFirst);
    }


    /**
     * 获取每周礼拜天
     *
     * @return
     */
    public static String getWeekLast(String nowDay) {
        LocalDate inputDate;
        if (!StringUtils.isEmpty(nowDay)) {
            inputDate = LocalDate.parse(nowDay);
        } else {
            inputDate = LocalDate.now();
        }
        TemporalAdjuster LAST_OF_WEEK = TemporalAdjusters.ofDateAdjuster(localDate -> localDate.minusDays(localDate.getDayOfWeek().getValue() - DayOfWeek.SUNDAY.getValue()));
        LocalDate with = inputDate.with(LAST_OF_WEEK);
        return with.toString();
    }


    public static String getWeekPriFirst(String nowDay) {
        LocalDate inputDate;
        if (!StringUtils.isEmpty(nowDay)) {
            inputDate = LocalDate.parse(nowDay);
        } else {
            inputDate = LocalDate.now();
        }
        DayOfWeek dayOfWeek = inputDate.getDayOfWeek();//获取今天是周几
        LocalDate lastMonday = inputDate.minusDays(7 + dayOfWeek.getValue() - 1);//算出上周一
        return lastMonday.toString();
    }


    public static boolean isValidDate(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        //String str = "2007-01-02";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(str);
            return str.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取上个礼拜天
     *
     * @return
     */
    public static String getWeekPriLast(String nowDay) {
        LocalDate inputDate;
        if (!StringUtils.isEmpty(nowDay)) {
            inputDate = LocalDate.parse(nowDay);
        } else {
            inputDate = LocalDate.now();
        }
        DayOfWeek dayOfWeek = inputDate.getDayOfWeek();//获取今天是周几
        LocalDate lastMonday = inputDate.minusDays(dayOfWeek.getValue());//算出上周日
        return lastMonday.toString();
    }


    public static void main(String[] args) {
//        LocalDate local = LocalDate.now();//获取当前时间;
//        System.out.println(getYesterdayDate());
//        local = LocalDate.parse("2018-07-30");//获取当前时间
//        DayOfWeek dayOfWeek = local.getDayOfWeek();//获取今天是周几
//        System.out.println(dayOfWeek.getValue());
//        LocalDate lastMonday = local.minusDays(dayOfWeek.getValue() - 1);//算出上周一
//        System.out.println(lastMonday);


    }

}
