package com.tawilib.app.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String LOCAL_DATE_TIME_FORMAT_1 = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z"; // i.e: Thu Aug 25 2022 11:05:39 GMT+0800

    public static LocalDateTime getLocalDateTimeObject(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute, second);
    }

    public static long getUnixTimeStamp(LocalDateTime localDateTime) { // Epoch time
        ZoneId zoneId = ZoneId.systemDefault();
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    public static String format(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.atZone(ZoneId.systemDefault()).format(formatter);
    }

    public static String format(long epoch, String format) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epoch), ZoneId.systemDefault());
        return format(localDateTime, format);
    }

}
