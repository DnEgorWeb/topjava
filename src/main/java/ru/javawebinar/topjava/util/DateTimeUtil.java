package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static boolean isBetweenHalfOpen(LocalDateTime ldt, LocalDateTime startTime, LocalDateTime endTime) {
        return ldt.compareTo(startTime) >= 0 && ldt.compareTo(endTime) < 0;
    }

    public static LocalDate parseLocalDate(String str, LocalDate fallbackDate) {
        try {
            return LocalDate.parse(str);
        } catch (Throwable err) {
            return fallbackDate;
        }
    }

    public static LocalTime parseLocalTime(String str, LocalTime fallbackTime) {
        try {
            return LocalTime.parse(str);
        } catch (Throwable err) {
            return fallbackTime;
        }
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

