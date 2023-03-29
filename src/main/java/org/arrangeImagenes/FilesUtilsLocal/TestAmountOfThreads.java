package org.arrangeImagenes.FilesUtilsLocal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestAmountOfThreads {

    public static void maxThreads(){
        ExecutorService serivce = Executors.newFixedThreadPool(Integer.MAX_VALUE);
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            serivce.submit(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(Integer.MAX_VALUE);
                    } catch (InterruptedException e) {
                    }
                }
            });
            System.out.println(i);
        }
    }
}
