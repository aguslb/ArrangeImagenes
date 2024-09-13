package org.arrangeImagenes.metadataExt;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.arrangeImagenes.util.DateRegex;

import java.io.File;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.arrangeImagenes.util.DateTimeFormatterEnum.*;
import static org.arrangeImagenes.util.Values.*;

@Log
public class MetadataUtil {

    String propertiesResultPath;


    public MetadataUtil(String propertiesResultPath) {
        this.propertiesResultPath = propertiesResultPath;
    }

    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot + 1);
        }
        return "";
    }

    public String getStrFromTags(File file, boolean isPath) {
        String ext = getFileExtension(file.getName()).toLowerCase();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            String resolutionW = "";
            String resolutionH = "";
            String date = "";

            for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    if (tag.getTagName().contains(IMAGE_WIDTH) && StringUtils.isBlank(resolutionW)) {
                        String trimW = tag.getDescription().replaceAll(PIXELS, "").trim();
                        int pixWidth = Integer.parseInt(trimW);
                        resolutionW = pixWidth >= PIX_MAX_WIDTH ? trimW + "x" : null;
                    } else if (tag.getTagName().contains(IMAGE_HEIGHT) && StringUtils.isBlank(resolutionH)) {
                        String trimH = tag.getDescription().replaceAll(PIXELS, "").trim();
                        int pixHeight = Integer.parseInt(trimH);
                        resolutionH = pixHeight >= PIX_MAX_HEIGHT ? trimH : null;
                    } else if (tag.getTagName().contains(DATE)) {
                        date = getDate(tag.getDescription(), date);
                    }
                }
            }
            if (StringUtils.isNotEmpty(resolutionH) || StringUtils.isNotEmpty(resolutionW)) {
                return isPath ? propertiesResultPath + File.separator + ext + File.separator + date + File.separator + resolutionW + resolutionH
                        : date + "_" + UUID.randomUUID().toString().toUpperCase() + "." + ext;
            } else
                return null;
        } catch (Exception e) {
            log.log(Level.SEVERE, file.getName() + " -- " + e.getMessage());
            return isPath ? propertiesResultPath + File.separator + NIL : UUID.randomUUID().toString().toUpperCase() + "." + ext;
        }
    }

    private String getDate(String line, String oldestString) {
        LocalDate date = null;
        LocalDate oldest;
        oldest = StringUtils.isBlank(oldestString) ?
                LocalDate.now() :
                LocalDate.parse(oldestString, DATE_TIME_FORMATTER_ENUM_PATH.dateTimeFormatterValue);
        String matchLine = normalize(line);
        int i = 1;
        for (DateRegex dateRegex : DateRegex.values()) {
            Pattern pattern = Pattern.compile(dateRegex.dateRegexValue);
            Matcher matcher = pattern.matcher(matchLine);
            if (matcher.matches()) {
                date = formatedDate(line, i);
                break;
            } else if (i == DateRegex.values().length) {
                date = formatedDate(oldest.toString(), 0);
            }
            i++;
        }
        assert date != null;
        if (date.isAfter(LocalDate.of(AFTER_YEAR, 1, 1)) && date.isBefore(oldest)) {
            oldest = date;
        }
        String day = oldest.getDayOfMonth() < 10 ? "0" + oldest.getDayOfMonth() : oldest.getDayOfMonth() + "";
        String month = oldest.getMonthValue() < 10 ? "0" + oldest.getMonthValue() : oldest.getMonthValue() + "";
        return oldest.getYear() + "_" + month + "_" + day;
    }

    private static String normalize(String str) {
        String normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        return normalizedString.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    private LocalDate formatedDate(String dateToFormat, int formater) {
        LocalDate date;
        switch (formater) {
            case 1:
                String year = dateToFormat.substring(dateToFormat.indexOf("-") + 7);
                dateToFormat = dateToFormat.substring(0, dateToFormat.indexOf("-"));
                dateToFormat = dateToFormat + year;
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_HR.dateTimeFormatterValue);
                break;
            case 2:
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_PATH.dateTimeFormatterValue);
                break;
            case 3:
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_DATE_TIME.dateTimeFormatterValue);
                break;
            case 4:
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_D.dateTimeFormatterValue);
                break;
            case 5:
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_DD.dateTimeFormatterValue);
                break;
            default:
                date = LocalDate.parse(dateToFormat, DATE_TIME_FORMATTER_ENUM_DASH.dateTimeFormatterValue);
        }
        return date;
    }
}
