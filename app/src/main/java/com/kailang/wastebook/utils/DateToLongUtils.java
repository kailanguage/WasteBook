package com.kailang.wastebook.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateToLongUtils {
    public static long dateToLong(String date) {
        Date toLong=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            toLong = sdf.parse(date);
        } catch (ParseException e) {
            toLong=new Date();
            e.printStackTrace();
        }
        Log.e("xxxDateToLongUtils",sdf.format(toLong));
        return toLong.getTime();
    }
}
