package org.arrangeImagenes.FilesUtilsLocal;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.arrangeImagenes.metadataExt.MetadataUtil;

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

    public static int getNumberOfThreads(int listSize, int totalFilesPerThread) {
        return (int) Math.ceil((double) listSize / totalFilesPerThread);
    }

    public synchronized void iteratePathMeta(List<File> files, MetadataUtil metadataUtil, String name) {
        int i = 0;
        for (File activeFile : files) {
            if (!activeFile.isDirectory()) {
                i++;
                threadMonitor.addProgress();
                String newPath = metadataUtil.getStrFromTags(activeFile, true);
                String newFileName = metadataUtil.getStrFromTags(activeFile, false);
                System.out.print(name + " ** tf: " + i + " -------> " + threadMonitor.getIntProgress() + "\r");
                if (StringUtils.isNotEmpty(newPath) || StringUtils.isNotEmpty(newFileName)) {
                    File fileDir = new File(newPath, newFileName);
                    if (!fileDir.getParentFile().exists()) {
                        createPath(newPath);
                    }
                    copyFile(activeFile, new File(newPath + File.separator + newFileName));
                } else {
                    log.warning("skip due low resolution boundaries \r");
                }
            }
        }
    }

    public synchronized static void createPath(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    public synchronized void copyFile(File fileFrom, File fileTo) {
        try {
            copy(fileFrom.toPath(),
                    fileTo.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    public synchronized static int getFilesPerThread(int totalPathFiles) {
        return (int) Math.ceil(Math.sqrt(totalPathFiles));
    }

    public synchronized static List<Path> listOfRegularFiles(Path originPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = list(Paths.get(originPath.toString())).filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return localFiles;
    }

    public synchronized static List<Path> listOfDirectories(Path originPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = walk(originPath).filter(Files::isDirectory).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off directories");
        }
        return localFiles;
    }

    public synchronized static int totalOfFiles(Path originPath) {
        try {
            return Files.walk(originPath).filter(Files::isRegularFile).toList().size();
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return 0;
    }
}
