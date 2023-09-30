package org.arrangeImagenes.FilesUtilsLocal;

import lombok.extern.java.Log;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
public class PhotoManagerThreaded implements Runnable {
    List<Path> path;
    ThreadMonitor threadMonitor;
    String name;
    String resultPath;
    File file;
    private Thread t;
    private final static String MSG_INIT = " -----> START";

    /**
     * @param path
     * @param threadMonitor
     */
    public PhotoManagerThreaded(List<Path> path, ThreadMonitor threadMonitor, String resultPath, File file) {
        this.path = path;
        this.threadMonitor = threadMonitor;
        this.name = UUID.randomUUID().toString();
        this.resultPath = resultPath;
        this.file = file;
    }

    /**
     *
     */
    @Override
    public void run() {
        log.info(name + " ---> thread" + MSG_INIT);
        FilesUtilsLocal filesUtilsLocal = new FilesUtilsLocal(threadMonitor, name, resultPath, file);
        filesUtilsLocal.iteratePath(path.stream().map(Path::toFile).collect(Collectors.toList()));
    }

    /**
     *
     */
    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
