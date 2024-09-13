package org.arrangeImagenes.util;

import lombok.extern.java.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.arrangeImagenes.util.Values.*;


@Log
public class ReadProperties {

    public static Properties readPropertiesFile(String os) {
        if (os.contains(WINDOWS))
            return readPropertiesFromProjectFile(WIN_APP_PROP);
        if (os.contains(OS_X))
            return readPropertiesFromProjectFile((MACOSX_APP_PROP));
        return readPropertiesFromProjectFile(UNIX_APP_PROP);
    }

    public static Properties readPropertiesFromProjectFile(String path) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(path)) {
            // load a properties file
            prop.load(input);
            for (Object key :
                    prop.keySet()) {
                System.setProperty(key.toString(), prop.getProperty(key.toString()));
            }
            return prop;
        } catch (IOException ex) {
            log.severe(ex.toString());
        }
        return prop;
    }
}
