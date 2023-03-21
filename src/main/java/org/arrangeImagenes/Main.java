package org.arrangeImagenes;

import org.arrangeImagenes.FilesUtilsLocal.PhotoManagerThreaded;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.TestAmountOfThreads;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    static Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        //TestAmountOfThreads.maxThreads();
        int from = 0;
        int to;
        String os = System.getProperty("os.name");
        logger.log(Level.INFO, "Start... " + os);
        Setting setting = new Setting(os);
        ThreadMonitor threadMonitor = new ThreadMonitor(setting.getTotalPathFiles());
        to = setting.getFilesPerThread();
        boolean nextBreak = false;
        for (int i = 0; i <= setting.getMaxThreads(); i++) {
            PhotoManagerThreaded photoManagerThreaded = new PhotoManagerThreaded(
                    setting.getFiles().subList(from, to), threadMonitor, setting.getResultPathString(), setting.getExifToolFile());
            photoManagerThreaded.start();
            from += setting.getFilesPerThread() + 1;
            to = from + setting.getFilesPerThread();
            if (nextBreak) {
                break;
            }
            if (to > setting.getTotalPathFiles()) {
                to = setting.getTotalPathFiles();
                nextBreak = true;
            }
        }
    }
}