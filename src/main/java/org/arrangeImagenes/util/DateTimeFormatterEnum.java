package org.arrangeImagenes.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.arrangeImagenes.util.DatePattern.*;

public enum DateTimeFormatterEnum {
    DATE_TIME_FORMATTER_ENUM_HR(DateTimeFormatter.ofPattern(PATTERN_DATE_HR.datePatternValue, new Locale("es", "ES"))),
    DATE_TIME_FORMATTER_ENUM_PATH(DateTimeFormatter.ofPattern(PATTERN_DATE_FOR_PATH.datePatternValue, Locale.ENGLISH)),
    DATE_TIME_FORMATTER_ENUM_DATE_TIME(DateTimeFormatter.ofPattern(PATTERN_DATE_TIME.datePatternValue, Locale.ENGLISH)),
    DATE_TIME_FORMATTER_ENUM_D(DateTimeFormatter.ofPattern(PATTERN_DATE_D.datePatternValue, Locale.ENGLISH)),
    DATE_TIME_FORMATTER_ENUM_DD(DateTimeFormatter.ofPattern(PATTERN_DATE_DD.datePatternValue, Locale.ENGLISH)),
    DATE_TIME_FORMATTER_ENUM_DASH(DateTimeFormatter.ofPattern(PATTERN_DATE_DASH.datePatternValue, Locale.ENGLISH));

    public final DateTimeFormatter dateTimeFormatterValue;

    DateTimeFormatterEnum(DateTimeFormatter dateTimeFormatterValue) {
        this.dateTimeFormatterValue = dateTimeFormatterValue;
    }
}
