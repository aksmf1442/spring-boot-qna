package com.codessquad.qna.util;

import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm");

    public static DateTimeFormatter getTimeFormatter() {
        return TIME_FORMATTER;
    }
}
