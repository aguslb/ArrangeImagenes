package org.arrangeImagenes.util;

public enum DateRegex {

    REGEX_DATE_HR ("^(sab|dom|lun|mar|mie|jue|vie) " +
            "(ene|feb|mar|abr|may|jun|jul|ago|sep|oct|nov|dic) " +
            "(0[1-9]|[12][0-9]|3[01]) " +
            "([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9] " +
            "[-+]\\d{2}:\\d{2} " +
            "\\d{4}$"),
     REGEX_DATE_FOR_PATH  ("^\\d{4}_(0[1-9]|1[0-2])_(0[1-9]|[12][0-9]|3[01])$"),
     REGEX_DATE_TIME  ("^\\d{4}:(0[1-9]|1[0-2]):(0[1-9]|[12][0-9]|3[01]) ([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$"),
     REGEX_DATE_D  ("^\\d{4}:(0[1-9]|1[0-2]):([1-9]|[12][0-9]|3[01])$"),
     REGEX_DATE_DD  ("^\\d{4}:(0[1-9]|1[0-2]):(0[1-9]|[12][0-9]|3[01])$");

    public final String dateRegexValue;

    DateRegex(String dateRegexValue) {
        this.dateRegexValue = dateRegexValue;
    }
}
