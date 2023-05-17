package com.google.drive.api.service;

import com.google.api.services.drive.model.File;
import com.google.drive.api.model.FileItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    private final DriveService driveService;
    private final CacheService cacheService;

    @Value("${google.played-songs-filename}")
    private String playedSongsFileName;

    @Autowired
    public ManagerService(DriveService driveService, CacheService cacheService, @Value("${google.songs-folder-id}") String songsFolderId) {
        this.driveService = driveService;
        this.cacheService = cacheService;

        //load the data in cache
        this.checkCacheAndLoadFileItems(songsFolderId);
    }

    public void checkCacheAndLoadFileItems(String songsFolderId) {
        if(cacheService.isCacheEmpty()) {
            driveService.getFilesFromFolder(songsFolderId)
                    .forEach(f -> cacheService.saveFileItem(
                            FileItem.builder()
                                    .fileId(f.getId())
                                    .name(f.getName())
                                    .build())
                    );

        }
    }


    public Map<String, List<FileItem>> getNextSongs() throws ExecutionException, InterruptedException {
        FileItem playedSongs = this.cacheService.findFileItemByName(playedSongsFileName);

        String[] nextSongsWithDate = driveService.getFileContent(playedSongs.getFileId()).split("//")[1].split(":");

        List<FileItem> fileItems = Arrays.stream(nextSongsWithDate[1].split(";"))
                .map(fileName -> {
                    fileName = fileName.trim().replaceAll("^\\n+|\\n+$", "");
                    FileItem song = this.cacheService.findFileItemByName(fileName);
                    return song;
                })
                .collect(Collectors.toList());


        if (this.filesAlreadyCached(fileItems)){
            return Map.of(nextSongsWithDate[0], fileItems);
        }
        else {
            List<FileItem> toBeCached = driveService.getFilesContent(fileItems);
            this.updateCacheWithContent(toBeCached);
            return Map.of(nextSongsWithDate[0], toBeCached);
        }
    }

    public void updateCacheWithContent(List<FileItem> fileItems) {
        fileItems.forEach(fileItem -> cacheService.updateItem(fileItem.getId(), fileItem.getContent()));
    }

    public boolean filesAlreadyCached(List<FileItem> fileItems) {
        for (FileItem fileItem : fileItems) {
            if (fileItem.getContent() == null) {
                return false;
            }
        }

        return true;
    }

}
