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

@Log
public class ComparePhotoThreaded implements Runnable {
    private Thread t;
    String name;
    List<String> path;

    ThreadMonitor threadMonitor;

    public ComparePhotoThreaded(List<String> path, ThreadMonitor threadMonitor) {
        this.path = path;
        this.name = UUID.randomUUID().toString();
        this.threadMonitor = threadMonitor;
    }

    @Override
    public void run() {
        for (String stringPath : path) {
            CompareSameSizeImage compareSameSizeImage = new CompareSameSizeImage();
            List<File> fileList = getFileListFromPathList(Path.of(stringPath));
            List<File> toDelete = new ArrayList<>();
            for (int i = 0; i < fileList.size(); i++) {
                log.info(stringPath + " <------" + i + "------- " + name + " - " + fileList.size());
                File initFile = fileList.get(i);
                if (!toDelete.isEmpty() && toDelete.stream().filter(file -> file.getName().equalsIgnoreCase(initFile.getName())).findFirst().isEmpty()) {
                    continue;
                }
                for (int j = 1; j < fileList.size(); j++) {
                    double percentage = compareSameSizeImage.compareSameSizeImage(initFile, fileList.get(j), name);
                    if (percentage <= 5) {
                        toDelete.add(fileList.get(j));
                    }
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
