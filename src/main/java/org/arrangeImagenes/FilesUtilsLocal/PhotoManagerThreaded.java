package org.arrangeImagenes.FilesUtilsLocal;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PhotoManagerThreaded implements Runnable {
    Logger logger = Logger.getLogger(PhotoManagerThreaded.class.getName());
    List<Path> path;
    ThreadMonitor threadMonitor;
    String name;
    private Thread t;
    private final static String MSG_INIT = " -----> START";

    private final static String MSG_END = " -----> DONE";

    /**
     * @param path
     * @param threadMonitor
     */
    public PhotoManagerThreaded(List<Path> path, ThreadMonitor threadMonitor) {
        this.path = path;
        this.threadMonitor = threadMonitor;
        this.name = UUID.randomUUID().toString();
    }

    /**
     *
     */
    @Override
    public void run() {
        logger.log(Level.INFO, name + "---> thread" + MSG_INIT);
        FilesUtilsLocal filesUtilsLocal = new FilesUtilsLocal(threadMonitor,name);
        filesUtilsLocal.iteratePath(path.stream().map(p -> p.toFile()).collect(Collectors.toList()));
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
