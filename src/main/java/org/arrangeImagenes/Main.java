package org.arrangeImagenes;

import lombok.extern.java.Log;
import org.arrangeImagenes.Compare.DeleteDuplicated;
import org.arrangeImagenes.FilesUtilsLocal.PhotoManagerThreaded;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

@Log
public class Main {


    /**
     * @param args
     */
    public static void main(String[] args) {
        //TestAmountOfThreads.maxThreads();
        int from = 0;
        int to;
        String os = System.getProperty("os.name");
        log.info("Start... " + os);
        Setting setting = new Setting(os);
        ThreadMonitor threadMonitor = new ThreadMonitor(setting.getTotalPathFiles());
        to = setting.getFilesPerThread();
        boolean nextBreak = false;
      /*  for (int i = 0; i <= setting.getMaxThreads(); i++) {
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
        }*/
        DeleteDuplicated deleteDuplicated = new DeleteDuplicated(setting);
        deleteDuplicated.deleteDuplicateImagesSamePath();
    }
}