package org.arrangeImagenes.Arrange;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.FilesUtilsLocal;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;
import org.arrangeImagenes.util.Setting;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static org.arrangeImagenes.util.Values.IMAGE_EXTENSIONS;

@Log
@AllArgsConstructor
public class ArrangeFiles {
    private Setting setting;

    public void arrangeFiles() {
        List<Path> originalListOfDirectories = FilesUtilsLocal.listOfDirectories(setting.getOriginalPath());
        ThreadMonitor threadMonitor = new ThreadMonitor(FilesUtilsLocal.totalOfFiles(setting.getOriginalPath()));
        IterateFiles iterateFiles = new IterateFiles(setting, threadMonitor);
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            int i = 0;
            for (Path activePath : originalListOfDirectories) {
                List<Path> listOfRegularFiles = FilesUtilsLocal.listOfRegularFiles(activePath);
                if (!listOfRegularFiles.isEmpty()) {
                    List<Path> listOfImages = listOfRegularFiles.stream()
                            .filter(ArrangeFiles::isImageFile)
                            .collect(Collectors.toList());
                    i++;
                    executorService.execute(new ThreadedIterator(iterateFiles, listOfImages, i));
                }

            }
        } catch (Exception e) {
            System.out.println("Interrupted");
        }
    }

    private static boolean isImageFile(Path path) {
        String fileName = path.getFileName().toString();
        String fileExtension = getFileExtension(fileName);
        return IMAGE_EXTENSIONS.contains(fileExtension.toLowerCase());
    }

    private static String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < fileName.length() - 1) {
            return fileName.substring(lastIndexOfDot + 1);
        }
        return "";
    }

}