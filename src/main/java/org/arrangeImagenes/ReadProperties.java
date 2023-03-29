package org.arrangeImagenes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

    private static final String WIN_APP_PROP = "C:\\Users\\zid_0\\IdeaProjects\\ArrangeImagenes\\src\\main\\resources\\application-win.properties";
    private static final String UNIX_APP_PROP = "/Users/agusmac/Proyectos/ArrangeImagenes/src/main/resources/application-unix.properties";

    public static Properties readPropertiesFile(String os) {
        if (os.contains("Windows"))
            return readPropertiesFromProjectFile(WIN_APP_PROP);
        return readPropertiesFromProjectFile(UNIX_APP_PROP);
    }

    public static Properties readPropertiesFromProjectFile(String path) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(path)) {
            // load a properties file
            prop.load(input);
            System.setProperties(prop);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

}
