package org.arrangeImagenes.FilesUtilsLocal;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.Files.*;

@AllArgsConstructor
@Log
public class FilesUtilsLocal {

    ThreadMonitor threadMonitor;

    String name;

    String resultPath;
    File file;

    public static int getNumberOfThreads(int listSize, int totalFilesPerThread) {
        return (int) Math.ceil((double) listSize / totalFilesPerThread);
    }


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
     * @param fileFrom
     * @param fileTo
     * @return
     */
    public synchronized boolean copyFile(File fileFrom, File fileTo) {
        try {
            copy(fileFrom.toPath(),
                    fileTo.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.warning(e.getMessage());
            return false;
        }
        return true;
    }

    public static int getFilesPerThread(int totalPathFiles) {
        return (int) Math.ceil(Math.sqrt(totalPathFiles));
    }

    public static List<Path> listOfRegularFiles(Path originPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = list(Paths.get(originPath.toString())).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return localFiles;
    }

    public static List<Path> listOfDirectories(Path originPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = walk(originPath).filter(Files::isDirectory).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off directories");
        }
        return localFiles;
    }

    public static int totalOfFiles(Path originPath) {
        try {
            return Files.walk(originPath).filter(Files::isRegularFile).collect(Collectors.toList()).size();
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return 0;
    }
}
