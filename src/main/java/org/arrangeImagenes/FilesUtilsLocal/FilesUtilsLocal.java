package org.arrangeImagenes.FilesUtilsLocal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilesUtilsLocal {

    Logger logger = Logger.getLogger(FilesUtilsLocal.class.getName());

    ThreadMonitor threadMonitor;

    String name;

    public FilesUtilsLocal(ThreadMonitor threadMonitor, String name) {
        this.threadMonitor = threadMonitor;
        this.name = name;
    }

    /**
     * @param files
     */
    public void iteratePath(List<File> files) {
        ExifToolUtil exifToolUtil = new ExifToolUtil();
        for (File activeFile : files) {
            if (!activeFile.isDirectory()) {
                threadMonitor.addProgress();
                System.out.print(name + " -------> " + threadMonitor.getIntProgress() + "\r");
                String newPath = exifToolUtil.getNewPathFromTags(activeFile);
                String newFileName = exifToolUtil.getNewNameFromTags(activeFile);
                File fileDir = new File(newPath, newFileName);
                boolean created;
                boolean moved = false;
                if (!fileDir.getParentFile().exists()) {
                    created = createPath(newPath);
                } else {
                    created = true;
                }
                if (created) {
                    moved = copyFile(activeFile, new File(newPath + File.separator + newFileName));
                } else {
                    logger.log(Level.WARNING, "path not created " + newPath);
                }
                if (!moved) {
                    logger.log(Level.WARNING, "file not moved " + activeFile.getName());
                }
            }
        }
    }

    /**
     * @param path
     * @return
     */
    public synchronized static boolean createPath(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) return theDir.mkdirs();
        return false;
    }

    /**
     * @param from
     * @param to
     * @return
     */
    public static boolean moveFile(File from, File to) {
        // renaming the file and moving it to a new location
        // if file copied successfully then delete the original file
        if (from.renameTo(to))
            return from.delete();
        return false;
    }

    /**
     * @param fileFrom
     * @param fileTo
     * @return
     */
    public synchronized boolean copyFile(File fileFrom, File fileTo) {
        try {
            Files.copy(fileFrom.toPath(),
                    fileTo.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.log(Level.WARNING, e.getMessage());
            return false;
        }
        return true;
    }
}
