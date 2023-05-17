package com.google.drive.api.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
class DriveServiceTest {
    private final DriveService driveService;

    @Value("${google.songs-folder-id}")
    private String songsFolderId;

    @Autowired
    DriveServiceTest(DriveService driveService) {
        this.driveService = driveService;
    }



    @Test
    void listFileContent() throws IOException {
        String fileContent = driveService.getFileContent(songsFolderId);
        Arrays.stream(fileContent.split("//")[1].split(";")).forEach(System.out::println);
    }

}