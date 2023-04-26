package org.arrangeImagenes.Compare;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Log
public class DeleteDuplicated {

    Setting setting;

    public void deleteDuplicateImagesSamePath() {
        String workingPath = setting.getResultPathString();
        List<Path> listOfDir = calculateListOfDirectories(workingPath);
        int totalDirs = listOfDir.size();
        int dirsPerThread = (int) Math.ceil((double) totalDirs / setting.getMaxThreads());
        int from = 0;
        int to = dirsPerThread;
        ThreadMonitor threadMonitor = new ThreadMonitor(listOfDir.size());
        boolean nextBreak = false;
        for (int i = 0; i < setting.getMaxThreads(); i++) {
            ComparePhotoThreaded comparePhotoThreaded = new ComparePhotoThreaded(listOfDir.subList(from, to), threadMonitor);
            comparePhotoThreaded.start();
            from += dirsPerThread + 1;
            to = from + dirsPerThread;
            if (nextBreak) {
                break;
            }
            if (to > totalDirs) {
                to = totalDirs;
                nextBreak = true;
            }
        }
    }

    private List<Path> calculateListOfDirectories(String workingPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = Files.walk(Path.of(workingPath)).filter(Files::isDirectory).filter(DeleteDuplicated::checkIfEmpty).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return localFiles;
    }

    private static boolean checkIfEmpty(Path directory) {
        try {
            return Files.list(directory)
                    .anyMatch(p -> !Files.isDirectory(p));
        } catch (IOException e) {
            return false;
        }
    }
}
