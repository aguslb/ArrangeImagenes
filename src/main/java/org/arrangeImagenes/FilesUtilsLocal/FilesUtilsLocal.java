package org.arrangeImagenes.FilesUtilsLocal;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

@AllArgsConstructor
@Log
public class FilesUtilsLocal {

    ThreadMonitor threadMonitor;

    String name;

    String resultPath;
    File file;

    /**
     * @param files
     */
    public void iteratePath(List<File> files) {
        ExifToolUtil exifToolUtil = new ExifToolUtil(resultPath, file);
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
                    log.warning("path not created " + newPath);
                }
                if (!moved) {
                    log.warning("file not moved " + activeFile.getName());
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
            log.warning(e.getMessage());
            return false;
        }
        return true;
    }
}
