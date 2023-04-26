package org.arrangeImagenes.FilesUtilsLocal;

import lombok.Getter;
import lombok.extern.java.Log;
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

@Getter
@Log
public class Setting {

    private final int totalPathFiles;

    private final int maxThreads;

    private final int filesPerThread;

    private final String scramblePath;

    private final String resultPathString;

    private final Path originalPath;

    private final File exifToolFile;

    private final List<Path> files;

    public Setting(String os) {
        log.info("Setting up properties");
        Properties properties = ReadProperties.readPropertiesFile(os);
        File dirResult = new File(properties.getProperty("RESULT_PATH"));
        if (!dirResult.exists())
            FilesUtilsLocal.createPath(dirResult.getAbsolutePath());
        scramblePath = properties.getProperty("ORIGINAL_PATH");
        resultPathString = dirResult.getAbsolutePath();
        originalPath = Paths.get(getScramblePath());
        files = calculateListOfFiles();
        totalPathFiles = files.size();
        maxThreads = Integer.parseUnsignedInt(properties.getProperty("MAX_THREADS"));
        filesPerThread = Integer.divideUnsigned(getTotalPathFiles(), getMaxThreads());
        exifToolFile = new File(properties.getProperty("exiftool.path"));

        log.info("Properties for this run: ");
        log.info(" -Local OS prop:");
        for (Map.Entry<Object, Object> propName : properties.entrySet()) {
            log.info(" --" + propName.getKey() + " : " + propName.getValue());
        }
        log.info("\n-scramblePath: " + scramblePath);
        log.info("-resultPath: " + resultPathString);
        log.info("-originalPath: " + originalPath);
        log.info("-totalPathFiles: " + totalPathFiles);
        log.info("-maxThreads: " + maxThreads);
        log.info("-filesPerThread: " + filesPerThread);
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
