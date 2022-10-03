package org.arrangeImagenes;

import org.arrangeImagenes.FilesUtilsLocal.PhotoManagerThreaded;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.util.logging.Logger;


public class Main {

    Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * @param args
     */
    public static void main(String[] args) {
        int from = 0;
        int to;
        Setting setting = new Setting();
        ThreadMonitor threadMonitor = new ThreadMonitor(setting.getTotalPathFiles());
        to = setting.getFilesPerThread();
        boolean nextBreak = false;
        for (int i = 0; i <= setting.getMaxThreads(); i++) {
            PhotoManagerThreaded photoManagerThreaded = new PhotoManagerThreaded(
                    setting.getFiles().subList(from, to), threadMonitor);
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