package edu.java.scrapper.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DateParser {
    public static OffsetDateTime parseDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss.")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 9, false)
            .appendPattern("x")
            .toFormatter();

        return OffsetDateTime.parse(date, formatter);
    }
}
