package org.arrangeImagenes.FilesUtilsLocal;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadMonitor {
    static Logger logger = Logger.getLogger(ThreadMonitor.class.getName());
    int total;
    int progress;

    public ThreadMonitor(int total) {
        logger.log(Level.INFO, "Start monitor");
        this.progress = 0;
        this.total = total;
        logger.log(Level.INFO, "values total: " + total + " progress:" + progress);
    }

    public synchronized void addProgress() {
        progress++;
    }

    public synchronized void setProgress(int progress) {
        this.progress = progress;
    }

    public synchronized int getProgress() {
        return progress;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public synchronized int getTotal() {
        return total;
    }

    public int getIntProgress() {
        return (100 * getProgress()) / getTotal();
    }
}
