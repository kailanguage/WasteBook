package com.kailang.wastebook.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateToLongUtils {

    public static long dateToLong(String date) {
        Date toLong = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            toLong = sdf.parse(date);
        } catch (ParseException e) {
            toLong = new Date();
            e.printStackTrace();
        }
        Log.e("xxxDateToLongUtils", sdf.format(toLong));
        return toLong.getTime();
    }

    public static String longToDate(long d) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(d));
        String now = sdf.format(new Date());
        if (date.contains(now.substring(0, 4))) date = date.substring(5, date.length());
        return date;
    }

    public static String longToDateAdd(long d){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(d));
        String now = sdf.format(new Date());
        return date;
    }

    public static String getSysYear() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.YEAR));
        return year;
    }

    public static String getSysMonth() {
        Calendar date = Calendar.getInstance();
        String year = String.valueOf(date.get(Calendar.MONTH)+1);
        return year;
    }
}
