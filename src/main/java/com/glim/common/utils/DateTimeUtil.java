package com.glim.common.utils;

import java.time.*;

public abstract class DateTimeUtil {

    public static String getTime(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        LocalDate saveDate = localDateTime.toLocalDate();
        LocalDate nowDate = now.toLocalDate();
        Period period = Period.between(saveDate, nowDate);

        LocalTime saveTime = localDateTime.toLocalTime();
        LocalTime nowTime = now.toLocalTime();
        Duration duration = Duration.between(saveTime, nowTime);

        String printDateAgo = getDateAgo(period);
        if(printDateAgo == null || (period.getDays() == 1 && duration.getSeconds() < 0)) {
            return localDateTime.toString().substring(11, 16);
        }
        return printDateAgo;
    }
    public static String getDateTimeAgo(LocalDateTime localDateTime) {
        LocalDateTime now = LocalDateTime.now();

        LocalDate saveDate = localDateTime.toLocalDate();
        LocalDate nowDate = now.toLocalDate();
        Period period = Period.between(saveDate, nowDate);

        LocalTime saveTime = localDateTime.toLocalTime();
        LocalTime nowTime = now.toLocalTime();
        Duration duration = Duration.between(saveTime, nowTime);

        String printDateAgo = getDateAgo(period);
        if(printDateAgo == null || (period.getDays() == 1 && duration.getSeconds() < 0)) {
            printDateAgo = getTimeAgo(duration);
        }
        return printDateAgo;
    }

    private static String getDateAgo(Period period) {
        String printDateAgo = null;
        if (period.getYears() > 0) {
            printDateAgo = period.getYears() + "년 전";
        }else if(period.getMonths() > 0) {
            printDateAgo = period.getMonths() + "달 전";
        }else if(period.getDays() > 0) {
            printDateAgo = period.getDays() + "일 전";
        }
        return printDateAgo;
    }

    private static String getTimeAgo(Duration duration) {
        String printDateAgo;
        long seconds = duration.getSeconds();
        if(seconds < 0){
            seconds += 86400;
        }
        if(seconds > 3600) {
            printDateAgo = (seconds / 3600) + "시간 전";
        }else if(seconds > 60) {
            printDateAgo = (seconds % 3600 / 60) + "분 전";
        }else{
            printDateAgo = (seconds % 60) + "초 전";
        }
        return printDateAgo;
    }
}
