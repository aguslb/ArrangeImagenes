package org.arrangeImagenes.Compare;

import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Log
public class ComparePhotoThreaded implements Runnable {
    private Thread t;
    String name;

    File[] filesList;
    ThreadMonitor threadMonitor;

    public ComparePhotoThreaded(File[] filesList, ThreadMonitor threadMonitor) {
        this.filesList = filesList;
        this.name = UUID.randomUUID().toString();
        this.threadMonitor = threadMonitor;
    }

    @Override
    public void run() {
        CompareSameSizeImage compareSameSizeImage = new CompareSameSizeImage();
        for (int i = 0; i < filesList.length; i++) {
            File initFile = filesList[i];
            for (int j = 1; j < filesList.length; j++) {
                try {
                    if (initFile != null && filesList[j] != null) {
                        double percentage = compareSameSizeImage.compareSameSizeImage(initFile, filesList[j]);
                        if (percentage == 0) {
                            filesList[j].delete();
                        }
                    }
                } catch (Exception e) {
                    log.severe("run::Something went wrong: " + e.getMessage());
                }
            }
            int x = 0;
            for (File file : toDelete) {
                if (file.delete())
                    log.info("Ok " + x);
                log.info("no ok " + x);
                x++;
            }
        }
    }
  
    private List<File> getFileListFromPathList(Path path) {
        List<File> fileSet = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path activePath : stream) {
                if (!Files.isDirectory(activePath)) {
                    fileSet.add(activePath.toFile());
                }
            }
        } catch (IOException ioException) {
            log.warning("Error en getFileListFromPathList " + name);
        }
        return fileSet;
    }
    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
