package org.arrangeImagenes.FilesUtilsLocal;

public class ThreadMonitor {

    int total;
    int progress;

    public ThreadMonitor(int total){
        this.progress = 0;
        this.total = total;
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
