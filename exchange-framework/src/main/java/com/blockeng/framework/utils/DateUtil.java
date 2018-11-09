package com.blockeng.framework.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static long getCurrentLeaveTime() throws ParseException {
        long currentTime = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateString = s.format(date);
        StringBuffer sb = new StringBuffer();
        sb.append(currentDateString).append(" 23:59:59");
        SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = s1.parse(sb.toString());
        long currentEndTime = d.getTime();
        return currentEndTime - currentTime;
    }

    public static void main(String[] args) {
        System.out.println(isTradeTime("00:00-20:59", "7"));
    }

    /**
     * 校验交易时间
     *
     * @param time  交易时间范围
     * @param weeks 交易周期
     * @return
     */
    public static boolean isTradeTime(String time, String weeks) {
        if (!StringUtils.isEmpty(weeks)) {
            DateTime currentTime = new DateTime();
            int week = currentTime.getDayOfWeek();
            if (!weeks.contains(String.valueOf(week))) {
                return false;
            }
        }
        if (!StringUtils.isEmpty(time)) {
            DateTime current = new DateTime();
            String[] times = time.split("-");
            DateTime start = new DateTime(new StringBuffer(current.toString("yyyy-MM-dd"))
                    .append("T").append(times[0]).append(":00").toString());
            DateTime end = new DateTime(new StringBuffer(current.toString("yyyy-MM-dd"))
                    .append("T").append(times[1]).append(":59").toString());
            if (!new Interval(start, end).contains(current)) {
                return false;
            }
        }
        return true;
    }
}
