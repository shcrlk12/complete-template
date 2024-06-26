package com.unison.scada.availability.global;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Locale;

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
            localDateTime += " 00:00:00";
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        else if(localDateTime.length() == "yyyy-MM".length())
        {
            localDateTime += "-01 00:00:00";
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        else if(localDateTime.length() == "yyyy".length())
        {
            localDateTime += "-01-01 00:00:00";
            return LocalDateTime.parse(localDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        return null;
    }

    public static LocalDateTime getWeeklyTime(String time){
        String[] timeSplits = time.split("-");

        // 2024년의 첫 번째 월요일을 기준으로 2주차 날짜 범위를 계산합니다.
        LocalDateTime date = LocalDateTime.of(Integer.parseInt(timeSplits[0]), 1, 1, 0, 0);

        return date.plusWeeks(Integer.parseInt(timeSplits[1]) - 1);
    }

    public static LocalDateTime getQuarterTime(String time){
        String[] timeSplits = time.split("-");
        if(timeSplits[1].equalsIgnoreCase("1")){
            return LocalDateTime.of(Integer.parseInt(timeSplits[0]), 1, 1, 0, 0);
        }else if(timeSplits[1].equalsIgnoreCase("2")) {
            return LocalDateTime.of(Integer.parseInt(timeSplits[0]), 4, 1, 0, 0);
        }else if(timeSplits[1].equalsIgnoreCase("3")) {
            return LocalDateTime.of(Integer.parseInt(timeSplits[0]), 7, 1, 0, 0);
        }else if(timeSplits[1].equalsIgnoreCase("4")) {
            return LocalDateTime.of(Integer.parseInt(timeSplits[0]), 10, 1, 0, 0);
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
