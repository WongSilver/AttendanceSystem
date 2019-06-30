package edu.wong.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    private static Date date;

    public static String getTime() {
        date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String time = format.format(date.getTime());
        return time;
    }
}
