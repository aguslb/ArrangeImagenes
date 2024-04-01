package org.arrangeImagenes;

import lombok.extern.java.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@Log
public class ReadProperties {

    private static final String WIN_APP_PROP = "C:\\Users\\zid_0\\Projects\\ArrangeImagenes\\src\\main\\resources\\application-win.properties";
    private static final String UNIX_APP_PROP = "/Users/agusmac/Proyectos/ArrangeImagenes/src/main/resources/application-unix.properties";

    private static final String MACOSX_APP_PROP = "/Users/agus/Proyectos/ArrangeImagenes/src/main/resources/application-osx.properties";

    public static Properties readPropertiesFile(String os) {
        if (os.contains("Windows"))
            return readPropertiesFromProjectFile(WIN_APP_PROP);
        if (os.contains("OS X"))
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
