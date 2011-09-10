package biz.neustar.pagerduty.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static Date now() {
        return new Date();
    }

    public static Date monthsAgo(int months) {
        return adjust(Calendar.MONTH, -months);
    }

    public static Date weeksAgo(int weeks) {
        return adjust(Calendar.WEEK_OF_YEAR, -weeks);
    }

    public static Date daysAgo(int days) {
        return adjust(Calendar.DAY_OF_YEAR, -days);
    }

    public static Date hoursAgo(int hours) {
        return adjust(Calendar.HOUR_OF_DAY, -hours);
    }

    public static Date minutesAgo(int minutes) {
        return adjust(Calendar.MINUTE, -minutes);
    }

    public static Date monthsFromNow(int months) {
        return adjust(Calendar.MONTH, months);
    }

    public static Date weeksFromNow(int weeks) {
        return adjust(Calendar.WEEK_OF_YEAR, weeks);
    }

    public static Date daysFromNow(int days) {
        return adjust(Calendar.DAY_OF_YEAR, days);
    }

    public static Date hoursFromNow(int hours) {
        return adjust(Calendar.HOUR_OF_DAY, hours);
    }

    public static Date minutesFromNow(int minutes) {
        return adjust(Calendar.MINUTE, minutes);
    }

    private static Date adjust(int field, int by) {
        Calendar c = Calendar.getInstance();
        c.add(field, by);
        return c.getTime();
    }
}
