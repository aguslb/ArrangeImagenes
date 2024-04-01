package org.arrangeImagenes.Arrange;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.FilesUtilsLocal;
import org.arrangeImagenes.FilesUtilsLocal.PhotoManagerThreaded;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Log
@AllArgsConstructor
public class ArrangeFiles {
    private Setting setting;

    public void arrangeFiles() {
        List<Path> originalListOfDirectories = FilesUtilsLocal.listOfDirectories(setting.getOriginalPath());
        ThreadMonitor threadMonitor = new ThreadMonitor(FilesUtilsLocal.totalOfFiles(setting.getOriginalPath()));
            for (Path activePath : originalListOfDirectories) {
                List<Path> listOfRegularFiles = FilesUtilsLocal.listOfRegularFiles(activePath);
                if (!listOfRegularFiles.isEmpty()) {
                    iterate(listOfRegularFiles, threadMonitor);
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000 + 1));
                    } catch (InterruptedException | IllegalArgumentException e) {
                        log.severe(e.getMessage());
                    }
                }
            }
    }

    private void iterate(List<Path> origianlList, ThreadMonitor threadMonitor) {
        int from = 0;
        int listSize = origianlList.size();
        int totalFilesPerThread = FilesUtilsLocal.getFilesPerThread(listSize);
        int to = totalFilesPerThread;
        boolean nextBreak = false;
        int numberOfThreads = FilesUtilsLocal.getNumberOfThreads(listSize, totalFilesPerThread);
        for (int i = 0; i < numberOfThreads && from < to; i++) {
            PhotoManagerThreaded photoManagerThreaded = new PhotoManagerThreaded(origianlList.subList(from, to), threadMonitor, setting.getResultPathString(), setting.getExifToolFile());
            photoManagerThreaded.start();
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
    }

    private void iterateSingle(List<Path> origianlList, ThreadMonitor threadMonitor) {
        PhotoManagerThreaded photoManagerThreaded = new PhotoManagerThreaded(origianlList, threadMonitor, setting.getResultPathString(), setting.getExifToolFile());
        photoManagerThreaded.start();

    }
}
