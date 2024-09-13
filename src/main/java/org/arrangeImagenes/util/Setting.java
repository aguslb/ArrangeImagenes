package org.arrangeImagenes.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.FilesUtilsLocal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import static org.arrangeImagenes.util.Values.*;

@Log
@Getter
@AllArgsConstructor
public class Setting {
    private final String originalPathString;
    private final String resultPathString;
    private final Path originalPath;

    public Setting(String os) {
        log.info("Setting up properties");
        Properties properties = ReadProperties.readPropertiesFile(os);
        File dirResult = new File(properties.getProperty(RESULT_PATH));
        if (!dirResult.exists()) FilesUtilsLocal.createPath(dirResult.getAbsolutePath());
        originalPathString = properties.getProperty(ORIGINAL_PATH);
        resultPathString = dirResult.getAbsolutePath();
        originalPath = Paths.get(getOriginalPathString());
        log.info("Properties for this run: ");
        log.info(" -Local OS prop:");
        for (Map.Entry<Object, Object> propName : properties.entrySet()) {
            log.info(" --" + propName.getKey() + " : " + propName.getValue());
        }
        log.info("\n-originalPath: " + getOriginalPathString());
        log.info("-resultPath: " + getResultPathString());
    }


}
