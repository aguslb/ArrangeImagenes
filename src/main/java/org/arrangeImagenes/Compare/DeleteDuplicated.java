package org.arrangeImagenes.Compare;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.walk;

@AllArgsConstructor
@Log
public class DeleteDuplicated {

    Setting setting;

    String[] extensions = {".JPG", ".PNG", ".GIF", ".WEBP", ".TIFF", ".PSD", ".RAW", ".BMP", ".HEIC", ".HEIF", ".INDD", ".JPEG", ".SVG", ".AI", ".EPS", ".PDF", ".MOV", ".MP4"};

    public DeleteDuplicated(Setting setting) {
        this.setting = setting;
    }

    public void deleteDuplicateImagesSamePath() {
        String workingPath = setting.getResultPathString();
        List<Path> listOfDir = calculateListOfDirectories(workingPath);
        ThreadMonitor threadMonitor = new ThreadMonitor(listOfDir.size());
        for (Path p : listOfDir) {
            File[] filesList = p.toFile().listFiles(f -> isEndWith(f.getAbsolutePath(), extensions));
            if (filesList.length > 0) {
                ComparePhotoThreaded comparePhotoThreaded = new ComparePhotoThreaded(filesList, threadMonitor);
                comparePhotoThreaded.start();
            }
        }
    }

    private List<Path> calculateListOfDirectories(String workingPath) {
        List<Path> localFiles = new ArrayList<>();
        try {
            localFiles = walk(Path.of(workingPath)).filter(Files::isDirectory).filter(DeleteDuplicated::checkIfEmpty).collect(Collectors.toList());
        } catch (IOException ioException) {
            log.severe("Error getting the list off Files");
        }
        return localFiles;
    }

    private static boolean checkIfEmpty(Path directory) {
        try {
            return Files.list(directory).anyMatch(p -> !Files.isDirectory(p));
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isEndWith(String file, String[] fileExtensions) {
        boolean result = false;
        for (String fileExtension : fileExtensions) {
            if (file.endsWith(fileExtension)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
