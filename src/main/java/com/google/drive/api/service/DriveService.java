package com.google.drive.api.service;

import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.drive.api.model.FileItem;
import com.google.drive.api.utils.GeneralUseMethods;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DriveService {

    private Drive driveAdapter;
    private Docs docsAdapter;

    public List<File> getFilesFromFolder(String folderId) {

        try {
            return driveAdapter.files()
                    .list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("files(name, id)")
                    .execute()
                    .getFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFileContent(String fileId) {
        try {
            Document response = docsAdapter.documents().get(fileId).execute();
            return GeneralUseMethods.readDocumentStructuralElements(response.getBody().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Docs.Documents.Get buildGetRequest(String fileId) {
        try {
            return docsAdapter.documents().get(fileId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<FileItem> getFilesContent(List<FileItem> fileItems) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(fileItems.size());
        List<Future<Document>> futures = new ArrayList<>();

        // Submit the document fetching tasks
        fileItems.forEach(fileItem -> {
            Docs.Documents.Get getRequest = this.buildGetRequest(fileItem.getFileId());
            Future<Document> future = executorService.submit(() -> getRequest.execute());
            futures.add(future);
        });


        // Process the document content
        for (int i = 0; i < futures.size(); i++) {
            Future<Document> future = futures.get(i);

            // Retrieve the document and extract the content
            Document document = future.get();
            fileItems.get(i).setContent(GeneralUseMethods.readDocumentStructuralElements(document.getBody().getContent()));
        }

        // Shutdown the executor service
        executorService.shutdown();

        return fileItems;
    }
}

