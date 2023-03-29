package org.arrangeImagenes.Compare;

import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Log
public class ComparePhotoThreaded implements Runnable {
    private Thread t;
    String name;
    List<Path> pathList;

    ThreadMonitor threadMonitor;

    public ComparePhotoThreaded(List<Path> path, ThreadMonitor threadMonitor) {
        this.pathList = path;
        this.name = UUID.randomUUID().toString();
        this.threadMonitor = threadMonitor;
    }

    @Override
    public void run() {
        CompareSameSizeImage compareSameSizeImage = new CompareSameSizeImage();
        List<File> fileList = getFileListFromPathList(pathList);
        for (int i = 0; i < fileList.size(); i++) {
            File initFile = fileList.get(i);
            for (int j = 1; j < fileList.size(); j++) {
                try {
                    double percentage = compareSameSizeImage.compareSameSizeImage(initFile, fileList.get(j));
                    if (percentage > 97) {
                        fileList.get(j).delete();
                    }
                } catch (IOException e) {
                    log.severe("run::Something went wrong: " + e.getMessage());
                }
            }
        }
    }

    private List<File> getFileListFromPathList(List<Path> pathList) {
        List<File> retList = new ArrayList<>();
        for (Path p : pathList) {
            try (Stream<Path> paths = Files.walk(Paths.get(p.toAbsolutePath().toString()))) {
                paths.filter(Files::isRegularFile).forEach(path -> retList.add(path.toFile()));
            } catch (IOException e) {
                log.severe("getFileListFromPathList::Something went wrong: " + e.getMessage());
            }
        }
        return retList;
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
