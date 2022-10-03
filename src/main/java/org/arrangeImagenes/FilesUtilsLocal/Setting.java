package org.arrangeImagenes.FilesUtilsLocal;

import org.arrangeImagenes.ReadProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Setting {

    Logger logger = Logger.getLogger(Setting.class.getName());

    private Properties properties;

    private int totalPathFiles;

    private int maxThreads;

    private int filesPerThread;

    private String scramblePath;

    private String resultPath;

    private File dirResult;

    private Path originalPath;

    private List<Path> files;

    public Setting() {
        properties = ReadProperties.readPropertiesFile();
        dirResult = new File(properties.getProperty("RESULT_PATH"));
        if (!dirResult.exists())
            FilesUtilsLocal.createPath(dirResult.getAbsolutePath());
        scramblePath = properties.getProperty("ORIGINAL_PATH");
        resultPath = getResultPath();
        originalPath = Paths.get(getScramblePath());
        files = calculateListOfFiles();
        totalPathFiles = files.size();
        maxThreads = Integer.parseUnsignedInt(properties.getProperty("MAX_THREADS"));
        filesPerThread = Integer.divideUnsigned(getTotalPathFiles(), getMaxThreads());
    }

    public int getFilesPerThread() {
        return filesPerThread;
    }

    public String getScramblePath() {
        return scramblePath;
    }

    public String getResultPath() {
        return resultPath;
    }

    public Path getOriginalPath() {
        return originalPath;
    }

    public int getTotalPathFiles() {
        return totalPathFiles;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public List<Path> getFiles() {
        return files;
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
