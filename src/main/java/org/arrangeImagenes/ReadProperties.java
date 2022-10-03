package org.arrangeImagenes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
    public static Properties readPropertiesFile(){
        FileInputStream fis = null;
        Properties prop = null;
        try {
            fis = new FileInputStream("application.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return prop;
    }

}
