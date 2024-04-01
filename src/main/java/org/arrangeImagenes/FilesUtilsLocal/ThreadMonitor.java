package org.arrangeImagenes.FilesUtilsLocal;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

@Log
@Setter
@Getter
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

    public int getIntProgress() {
        return (100 * getProgress()) / getTotal();
    }
}
