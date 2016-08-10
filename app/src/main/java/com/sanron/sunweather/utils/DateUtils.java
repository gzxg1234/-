package com.sanron.sunweather.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static Date parse(String str, String format) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * @return 0-8点 return 1;
     * 8-20 return 2;
     * 20-24 return 3;
     */
    public static int getTimeInterval() {
        Date nowDate = new Date();
        if (nowDate.getHours() >= 0 && nowDate.getHours() < 8) {
            //昨天的晚上，比如21日0-8点算20日的晚上
            return 1;
        } else if (nowDate.getHours() >= 8 && nowDate.getHours() < 20) {
            //今天的白天
            return 2;
        } else {
            //今天的晚上
            return 3;
        }
    }

    public static Date addDate(Date date, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, value);
        return calendar.getTime();
    }
}
