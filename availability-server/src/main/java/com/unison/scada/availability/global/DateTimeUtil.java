package com.unison.scada.availability.global;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class DateTimeUtil {

    public static LocalDateTime parseLocalDateTimeSeconds(String localDateTime){

        if(localDateTime.length() == "yyyy-MM-dd HH:mm:ss".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        else if(localDateTime.length() == "yyyy-MM-dd HH:mm".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        else if(localDateTime.length() == "yyyy-MM-dd HH".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        }
        else if(localDateTime.length() == "yyyy-MM-dd".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        else if(localDateTime.length() == "yyyy-MM".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        else if(localDateTime.length() == "yyyy".length())
        {
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy"));
        }

        return null;

    }
    public static String formatToYearMonthDayHourMinuteSecond(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    public static String formatToYearMonthDayHourMinute(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String formatToYearMonthDayHour(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
    }


    public static String formatToYearMonthDay(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String formatToYearMonth(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    public static String formatToYearWeekly(LocalDateTime localDateTime){
        int weekOfYear = localDateTime.get(ChronoField.ALIGNED_WEEK_OF_YEAR);

        return String.format("%04d-%02d", localDateTime.getYear(), weekOfYear);
    }
    public static String formatToYearQuarter(LocalDateTime localDateTime){
        if(localDateTime.getMonth().getValue() <= 3)
            return String.format("%04d-1", localDateTime.getYear());
        if(localDateTime.getMonth().getValue() <= 6)
            return String.format("%04d-2", localDateTime.getYear());
        if(localDateTime.getMonth().getValue() <= 9)
            return String.format("%04d-3", localDateTime.getYear());
        return String.format("%04d-4", localDateTime.getYear());
    }

    public static String formatToYear(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy"));
    }

    public static String formatToCustomPattern(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }
}
