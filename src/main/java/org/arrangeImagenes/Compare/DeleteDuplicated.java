package org.arrangeImagenes.Compare;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.arrangeImagenes.FilesUtilsLocal.Setting;
import org.arrangeImagenes.FilesUtilsLocal.ThreadMonitor;

import java.util.List;

@AllArgsConstructor
@Log
public class DeleteDuplicated {

    Setting setting;

    public void deleteDuplicateImagesSamePath() {
        List<String> workingPath = setting.getStringListPathDuplicates();
        ThreadMonitor threadMonitor = new ThreadMonitor(workingPath.size());
        int size = 300;
        for (int start = 0; start < workingPath.size(); start += size) {
            int end = Math.min(start + size, workingPath.size());
            ComparePhotoThreaded comparePhotoThreaded = new ComparePhotoThreaded(workingPath.subList(start, end), threadMonitor);
            comparePhotoThreaded.start();
        }
    }
}
