package org.arrangeImagenes;

import lombok.extern.java.Log;
import org.arrangeImagenes.Compare.DeleteDuplicated;
import org.arrangeImagenes.FilesUtilsLocal.PhotoManagerThreaded;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.util.Scanner;

@Log
public class Main {
    public static void main(String[] args) {
        //TestAmountOfThreads.maxThreads();
        String os = System.getProperty("os.name");
        log.info("Start... " + os);
        Setting setting = new Setting(os);
        ThreadMonitor threadMonitor = new ThreadMonitor(setting.getTotalPathFiles());
        String line = "";
        while (!line.equalsIgnoreCase("x")) {
            Scanner keyboard = new Scanner(System.in);
            log.severe("Enter an option:");
            log.severe("A: for arrange images");
            log.severe("D: for duplicate");
            log.severe("X: for exit");
            line = keyboard.nextLine();
            if (line.equalsIgnoreCase("A")) {
                runArrange(setting, threadMonitor);
            } else if (line.equalsIgnoreCase("D")) {
                runDuplicated(setting);
            }
        }
    }

    public static void runArrange(Setting setting, ThreadMonitor threadMonitor){
        int from = 0;
        int to;
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

    public static void runDuplicated(Setting setting){
        log.warning("Start Duplicated");
        DeleteDuplicated deleteDuplicated = new DeleteDuplicated(setting);
        deleteDuplicated.deleteDuplicateImagesSamePath();
    }
}