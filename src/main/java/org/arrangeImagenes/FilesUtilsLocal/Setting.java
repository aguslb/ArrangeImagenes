package org.arrangeImagenes.FilesUtilsLocal;

import lombok.Getter;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
public class Setting {

    static Logger logger = Logger.getLogger(Setting.class.getName());

    private final int totalPathFiles;

    private final int maxThreads;

    private final int filesPerThread;

    private final String scramblePath;

    private final String resultPathString;

    private final Path originalPath;

    private final File exifToolFile;

    private final List<Path> files;

    public Setting(String os) {
        logger.log(Level.INFO, "Setting up properties");
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

        logger.log(Level.INFO, "Properties for this run: ");
        logger.log(Level.INFO, " -Local OS prop:");
        for (Map.Entry<Object, Object> propName : properties.entrySet()) {
            logger.log(Level.INFO, " --" + propName.getKey() + " : " + propName.getValue());
        }
        logger.log(Level.INFO, "\n-scramblePath: " + scramblePath);
        logger.log(Level.INFO, "-resultPath: " + resultPathString);
        logger.log(Level.INFO, "-originalPath: " + originalPath);
        logger.log(Level.INFO, "-totalPathFiles: " + totalPathFiles);
        logger.log(Level.INFO, "-maxThreads: " + maxThreads);
        logger.log(Level.INFO, "-filesPerThread: " + filesPerThread);
    }

    private List<Path> calculateListOfFiles() {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = Files.walk(getOriginalPath()).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Error getting the list off Files");
        }
        return localFiles;
    }
}
