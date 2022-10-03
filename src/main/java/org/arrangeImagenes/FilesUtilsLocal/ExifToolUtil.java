package org.arrangeImagenes.FilesUtilsLocal;

import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import org.apache.commons.lang3.StringUtils;
import org.arrangeImagenes.Main;
import org.arrangeImagenes.ReadProperties;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExifToolUtil {
    Logger logger = Logger.getLogger(ExifToolUtil.class.getName());
    private Properties properties = ReadProperties.readPropertiesFile();

    /**
     *
     * @param file
     * @return
     */
    public synchronized String getNewPathFromTags(File file) {
        List<String> retPathName = getDataFromTags(file);
        return properties.getProperty("RESULT_PATH") + retPathName.get(0).toUpperCase() + File.separator + retPathName.get(1).toUpperCase() + File.separator + retPathName.get(2).toUpperCase();
    }

    /**
     *
     * @param file
     * @return
     */
    public synchronized String getNewNameFromTags(File file) {
        List<String> retPathName = getDataFromTags(file);
        return retPathName.get(1).toUpperCase() + UUID.randomUUID().toString().toUpperCase() + "." + retPathName.get(0);
    }

    /**
     *
     * @param file
     * @return
     */
    private List<String> getDataFromTags(File file) {
        List<String> returnList = new ArrayList<>();
        String dateCreated = "";
        String imageSize = "";
        String fileTypeExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1).toUpperCase();
        returnList.add(0, fileTypeExtension);
        try {
            Map<Tag, String> parse = parse(file);
            dateCreated = getDateToPutOnTag(file.getAbsolutePath());
            returnList.add(1, dateCreated);
            imageSize = parse.get(StandardTag.IMAGE_WIDTH) + "_" + parse.get(StandardTag.IMAGE_HEIGHT);
            if (StringUtils.isBlank(imageSize)) imageSize = "NONE";
            returnList.add(2, imageSize);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception occur when getNewNameFromTags", e);
        }
        return returnList;
    }

    /**
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    private String getDateToPutOnTag(String filePath) throws Exception {
        final String regexReplaceColon = "[\\D]+";
        final String datePattern = "yyyy:MM:dd";
        final String command = "exiftool";
        final String colon = ":";
        final String regexNonZero = "\\d\\d\\d[^0]:\\d[^0]:\\d[^0]";
        final Pattern pattern = Pattern.compile(regexNonZero);
        String retString = "";
        String line = "";
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern, Locale.ENGLISH);
            LocalDate date;
            LocalDate oldest = LocalDate.now();
            ProcessBuilder pb = new ProcessBuilder(command, filePath);
            Process p = pb.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = in.readLine()) != null) {
                line = line.toLowerCase();
                if (line.indexOf("date") >= 0) {
                    int colonIndexOf = line.indexOf(colon);
                    line = line.substring(colonIndexOf + 2);
                    if (line.length() >= 10) {
                        line = line.substring(0, 10);
                        line = line.replaceAll(regexReplaceColon, colon);
                        final Matcher matcher = pattern.matcher(line);
                        if (line.length() == 10 && matcher.matches()) {
                            date = LocalDate.parse(line, formatter);
                            if (date.isBefore(oldest)) {
                                oldest = date;
                            }
                        }
                    }
                }
            }
            p.waitFor();
            in.close();

            retString = oldest.getYear() + "_" + oldest.getMonthValue() + "_" + oldest.getDayOfMonth();
            return retString;
        } catch (Exception e) {
            logger.log(Level.SEVERE, line + " --->" + filePath);
            throw new Exception(e);
        }
    }

    /**
     *
     * @param image
     * @return
     * @throws Exception
     */
    private static Map<Tag, String> parse(File image) throws Exception {
        // ExifTool path must be defined as a system property (`exiftool.path`),
        // but path can be set using `withPath` method.
        try (ExifTool exifTool = new ExifToolBuilder().build()) {
            return exifTool.getImageMeta(image, Arrays.asList(StandardTag.GPS_ALTITUDE, StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.IMAGE_HEIGHT, StandardTag.IMAGE_WIDTH));
        }
    }

}
