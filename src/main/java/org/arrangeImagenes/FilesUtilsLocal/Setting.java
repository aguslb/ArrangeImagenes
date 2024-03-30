package org.arrangeImagenes.FilesUtilsLocal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.arrangeImagenes.ReadProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log
@Getter
@AllArgsConstructor
public class Setting {

    private final int totalPathFiles;

    private final int maxThreads;

    private final int filesPerThread;

    private final String originalPathString;

    private final String resultPathString;

    private final Path originalPath;

    private final File exifToolFile;

    private final List<Path> files;

    private final List<String> stringListPathDuplicates ;

    public Setting(String os) {
        log.info("Setting up properties");
        Properties properties = ReadProperties.readPropertiesFile(os);
        File dirResult = new File(properties.getProperty("RESULT_PATH"));
        if (!dirResult.exists()) FilesUtilsLocal.createPath(dirResult.getAbsolutePath());
        originalPathString = properties.getProperty("ORIGINAL_PATH");
        resultPathString = dirResult.getAbsolutePath();
        originalPath = Paths.get(getOriginalPathString());
        stringListPathDuplicates = getListFrom((String) properties.get("LIST_DUPLICATES_PATH"));
        files = calculateListOfFiles();
        totalPathFiles = files.size();
        maxThreads = Integer.parseUnsignedInt(properties.getProperty("MAX_THREADS"));
        filesPerThread = getPerThread();
        exifToolFile = new File(properties.getProperty("exiftool.path"));

        log.info("Properties for this run: ");
        log.info(" -Local OS prop:");
        for (Map.Entry<Object, Object> propName : properties.entrySet()) {
            log.info(" --" + propName.getKey() + " : " + propName.getValue());
        }
        log.info("\n-originalPath: " + getOriginalPathString());
        log.info("-resultPath: " + getResultPathString());
        log.info("-totalPathFiles: " + totalPathFiles);
        log.info("-maxThreads: " + maxThreads);
        log.info("-filesPerThread: " + filesPerThread);
    }

    private List<String> getListFrom(String listDuplicatesPath) {
        List<String> ret = new ArrayList<>();
        try {
            Files.walk(Path.of(listDuplicatesPath)).filter(Files::isDirectory).forEach( f -> ret.add(f.toFile().getAbsolutePath()));
           } catch (IOException ioException) {
        log.severe("Error getting the list off Files");
    }
        return ret;
    }

    private int getPerThread() {
        return  (int) Math.ceil(Math.sqrt(getTotalPathFiles()));
    }

    private List<Path> calculateListOfFiles() {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = Files.walk(getOriginalPath()).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return localFiles;
    }
}
