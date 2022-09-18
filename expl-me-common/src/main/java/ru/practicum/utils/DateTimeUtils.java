package ru.practicum.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

    // Регулярное выражение на проверку формата строки для даты и времени вида: 9999-99-99 99:99:99
    public static final String DATETIME_REGEXP =
            "^\\d\\d\\d\\d-\\d\\d-\\d\\d\\s\\d\\d:\\d\\d:\\d\\d$";

    private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static LocalDateTime strToDateTime(String value) {
        if (value == null) {
            return null;
        } else {
            return LocalDateTime.parse(value, formatter);
        }
    }

    public static String dateTimeToStr(LocalDateTime value) {

        if (value == null) {
            return null;
        } else {
            return formatter.format(value);
        }

    }

}
