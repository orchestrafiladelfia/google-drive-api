package com.google.drive.api.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.drive.api.model.FileItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

@Configuration
public class DriveApiConfiguration {
    @Value("${google.service.account.key}")
    private Resource serviceAccountKeyPath;

    @Value("${google.application-name}")
    private String applicationName;

    @Bean
    public GoogleCredential googleCredential() throws IOException {
        return GoogleCredential.fromStream(serviceAccountKeyPath.getInputStream())
                .createScoped(Arrays.asList(DriveScopes.DRIVE));
    }

    @Bean
    public Drive driveAdapter()
            throws GeneralSecurityException, IOException {

        Drive drive = new Drive.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                googleCredential())
                .setApplicationName(applicationName)
                .build();
        return drive;
    }

    @Bean
    public Docs docsAdapter()
            throws GeneralSecurityException, IOException {

        Docs docsAdapter = new Docs.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                googleCredential())
                .setApplicationName(applicationName)
                .build();
        return docsAdapter;
    }
}
