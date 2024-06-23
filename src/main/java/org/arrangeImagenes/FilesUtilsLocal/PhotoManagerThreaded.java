package org.arrangeImagenes.FilesUtilsLocal;

import lombok.extern.java.Log;
import org.arrangeImagenes.metadataExt.MetadataUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log
public class PhotoManagerThreaded extends Thread {

    FilesUtilsLocal filesUtilsLocal;
    MetadataUtil metadataUtil;
    List<Path> path;

    String nameCombinedInt;
    private Thread t;

    public PhotoManagerThreaded(FilesUtilsLocal filesUtilsLocal, List<Path> path,  String nameCombinedInt, MetadataUtil metadataUtil) {
        this.filesUtilsLocal = filesUtilsLocal;
        this.path = path;
        this.nameCombinedInt = nameCombinedInt;
        this.metadataUtil = metadataUtil;
    }

    @Override
    public void run() {
        filesUtilsLocal.iteratePathMeta(path.stream().map(Path::toFile).collect(Collectors.toList()), metadataUtil, UUID.randomUUID() + " -- " + nameCombinedInt);
    }

    public void start() {
        if (t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
