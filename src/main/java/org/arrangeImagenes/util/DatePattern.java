package org.arrangeImagenes.util;

public enum DatePattern {
    PATTERN_DATE_HR ("EEE MMM dd HH:mm:ss yyyy"),
    PATTERN_DATE_FOR_PATH ("yyyy_MM_dd"),
    PATTERN_DATE_TIME ("yyyy:MM:dd HH:mm:ss"),
    PATTERN_DATE_D ("yyyy:MM:d"),
    PATTERN_DATE_DD ("yyyy:MM:dd"),
    PATTERN_DATE_DASH ("yyyy-MM-dd");

    public final String datePatternValue;

    DatePattern(String datePatternValue) {
        this.datePatternValue = datePatternValue;
    }
}
