package org.arrangeImagenes;

import lombok.extern.java.Log;
import org.arrangeImagenes.Arrange.ArrangeFiles;
import org.arrangeImagenes.util.Setting;

import java.util.Scanner;

@Log
public class Main {
    public static void main(String[] args) {
        String os = System.getProperty("os.name");
        log.info("Start... " + os);
        Setting setting = new Setting(os);
        String line = "";
        while (!line.equalsIgnoreCase("x")) {
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter an option:");
            System.out.println("A: for arrange images");
            System.out.println("X: for exit");
            line = keyboard.nextLine();
            if (line.equalsIgnoreCase("A")) {
                runArrange(setting);
            }
        }
    }

    public static void runArrange(Setting setting) {
        log.warning("Start Arrange");
        ArrangeFiles arrangeFiles = new ArrangeFiles(setting);
        arrangeFiles.arrangeFiles();
    }

}