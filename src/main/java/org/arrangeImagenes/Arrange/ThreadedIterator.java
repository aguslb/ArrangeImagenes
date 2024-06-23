package org.arrangeImagenes.Arrange;

import lombok.extern.java.Log;

import java.nio.file.Path;
import java.util.List;

@Log
public class ThreadedIterator extends Thread {
    private Thread t;
    final IterateFiles iterateFiles;

    List<Path> listOfRegularFiles;
    int i;

    public ThreadedIterator(IterateFiles iterateFiles, List<Path> listOfRegularFiles, int i) {
        this.iterateFiles = iterateFiles;
        this.listOfRegularFiles = listOfRegularFiles;
        this.i = i;
    }

    @Override
    public void run() {
            iterateFiles.iterate(listOfRegularFiles, i);
    }


    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
