package com.hy.jame.springdatajpaplus.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * author：  HyZhan
 * create：  2019/4/17
 * desc：    TODO
 */
public class DateUtils {

    public static String parseTime(Date createTime) {
        return parseTime(createTime.getTime());
    }

    public static String parseTime(long publishTime) {
        long time;
        time = (System.currentTimeMillis() - publishTime) / 1000;
        int day, hour, minute, second;
        day = (int) (time / 3600 / 24);

        if (day == 1)
            return "1天前";
        if (day > 1)
            return new SimpleDateFormat("yyyy-MM-dd").format(publishTime);
        hour = (int) (time / 3600);
        if (hour > 0)
            return hour + "小时前";
        minute = (int) (time / 60);
        if (minute > 0)
            return minute + "分钟前";
        second = (int) time;
        if (second >= 0)
            return "刚刚";
        return "";
    }

    public static Date convertTime(int time) {
        long currentTime = System.currentTimeMillis();
        currentTime += time;
        return new Date(currentTime);
    }

    public static Date stringFormat(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (time == null)
            return null;

        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

