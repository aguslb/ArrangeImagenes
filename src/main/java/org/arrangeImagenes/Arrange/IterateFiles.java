package org.arrangeImagenes.Arrange;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.*;
import org.arrangeImagenes.metadataExt.MetadataUtil;
import org.arrangeImagenes.util.Setting;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Log
@AllArgsConstructor
public class IterateFiles {
    private Setting setting;
    private ThreadMonitor threadMonitor;

    public void iterate(List<Path> origianlList, int j) {
        int from = 0;
        int listSize = origianlList.size();
        int totalFilesPerThread = FilesUtilsLocal.getFilesPerThread(listSize);
        int to = totalFilesPerThread;
        boolean nextBreak = false;
        int numberOfThreads = FilesUtilsLocal.getNumberOfThreads(listSize, totalFilesPerThread);
        MetadataUtil metadataUtil = new MetadataUtil(setting.getResultPathString());
        FilesUtilsLocal filesUtilsLocal = new FilesUtilsLocal(threadMonitor);
        try (ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            for (int i = 0; i < numberOfThreads && from < to; i++) {
                executorService.execute(new PhotoManagerThreaded(filesUtilsLocal, origianlList.subList(from, to),  " tdir: " + j + " ** tdf:" + i, metadataUtil));
                from += totalFilesPerThread + 1;
                to = from + totalFilesPerThread;
                if (nextBreak) {
                    break;
                }
                if (to > listSize) {
                    to = listSize;
                    nextBreak = true;
                }
            }
            executorService.shutdown();
            try {
                executorService.awaitTermination(30, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                log.log(Level.SEVERE, e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Interrupted");
        }
    }
}
