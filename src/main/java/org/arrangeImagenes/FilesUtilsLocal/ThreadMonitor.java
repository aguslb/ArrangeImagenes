package org.arrangeImagenes.FilesUtilsLocal;

import lombok.extern.java.Log;

@Log
public class ThreadMonitor {
    int total;
    int progress;

    public ThreadMonitor(int total) {
        log.info("Start monitor");
        this.progress = 0;
        this.total = total;
        log.info("values total: " + total + " progress:" + progress);
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
