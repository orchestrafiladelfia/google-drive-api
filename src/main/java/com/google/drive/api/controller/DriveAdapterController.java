package com.google.drive.api.controller;

import com.google.drive.api.model.FileItem;
import com.google.drive.api.service.DriveService;
import com.google.drive.api.service.ManagerService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class DriveAdapterController {
    private final ManagerService managerService;

    @Value("${google.songs-folder-id}")
    private String songsFolderId;

    @GetMapping(value = "/getNextSongs", produces = "application/json")
    @ResponseBody
    public Map<String, List<FileItem>> getNextSongs() throws IOException, ExecutionException, InterruptedException {
        return managerService.getNextSongs();
    }
}
