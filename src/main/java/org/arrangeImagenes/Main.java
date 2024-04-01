package org.arrangeImagenes;

import lombok.extern.java.Log;
import org.arrangeImagenes.Arrange.ArrangeFiles;
import org.arrangeImagenes.Compare.DeleteDuplicated;
import org.arrangeImagenes.FilesUtilsLocal.Setting;

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
            log.severe("Enter an option:");
            log.severe("A: for arrange images");
            log.severe("D: for duplicate");
            log.severe("X: for exit");
            line = keyboard.nextLine();
            if (line.equalsIgnoreCase("A")) {
                runArrange(setting);
            } else if (line.equalsIgnoreCase("D")) {
                runDuplicated(setting);
            }
        }
    }

    public static void runArrange(Setting setting) {
        log.warning("Start Arrange");
        ArrangeFiles arrangeFiles = new ArrangeFiles(setting);
        arrangeFiles.arrangeFiles();
    }

    public static void runDuplicated(Setting setting) {
        log.warning("Start Duplicated");
        DeleteDuplicated deleteDuplicated = new DeleteDuplicated(setting);
        deleteDuplicated.deleteDuplicateImagesSamePath();
    }
}