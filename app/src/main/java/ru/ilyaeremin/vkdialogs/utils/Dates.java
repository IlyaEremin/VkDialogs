package ru.ilyaeremin.vkdialogs.utils;

import java.util.Calendar;
import java.util.Date;

import ru.ilyaeremin.vkdialogs.utils.times.FastDateFormat;

/**
 * Created by Ilya Eremin on 1/22/16.
 */
public class Dates {

    public static FastDateFormat formatterDay;
    public static FastDateFormat formatterWeek;
    public static FastDateFormat formatterMonth;
    public static FastDateFormat formatterYear;

    public static String stringForMessageListDate(long date) {
        try {
            Calendar rightNow = Calendar.getInstance();
            int day = rightNow.get(Calendar.DAY_OF_YEAR);
            int year = rightNow.get(Calendar.YEAR);
            rightNow.setTimeInMillis(date * 1000);
            int dateDay = rightNow.get(Calendar.DAY_OF_YEAR);
            int dateYear = rightNow.get(Calendar.YEAR);

            if (year != dateYear) {
                return formatterYear.format(new Date(date * 1000));
            } else {
                int dayDiff = dateDay - day;
                if(dayDiff == 0 || dayDiff == -1 && (int)(System.currentTimeMillis() / 1000) - date < 60 * 60 * 8) {
                    return formatterDay.format(new Date(date * 1000));
                } else if(dayDiff > -7 && dayDiff <= -1) {
                    return formatterWeek.format(new Date(date * 1000));
                } else {
                    return formatterMonth.format(new Date(date * 1000));
                }
            }
        } catch (Exception e) {
            DLogger.e("tmessages", e);
        }
        return "LOC_ERR";
    }
}
