package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static <T extends Comparable<T>> boolean isBetweenHalfOpen(T origin, T start, T end) {
        return origin.compareTo(start) >= 0 && origin.compareTo(end) < 0;
    }

    public static LocalDate parseLocalDate(String str) {
        try {
            return LocalDate.parse(str);
        } catch (Throwable err) {
            return null;
        }
    }

    public static LocalTime parseLocalTime(String str) {
        try {
            return LocalTime.parse(str);
        } catch (Throwable err) {
            return null;
        }
    }

    public static String toString(LocalDateTime ldt) {
        return ldt == null ? "" : ldt.format(DATE_TIME_FORMATTER);
    }
}

