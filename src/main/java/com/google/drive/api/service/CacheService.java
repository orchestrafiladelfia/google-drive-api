package com.google.drive.api.service;

import com.google.drive.api.model.FileItem;
import com.google.drive.api.repository.FileItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final FileItemRepository fileItemRepository;

    public void saveFileItem(FileItem fileItem) {
        fileItemRepository.save(fileItem);
    }

    public List<FileItem> getAllItems() {
        return fileItemRepository.findAll();
    }

    public FileItem findFileItemByName(String name) {
        return fileItemRepository.findByName(name);
    }

    public boolean isCacheEmpty() {
        return fileItemRepository.count() == 0;
    }

    public void clearCache() {
        fileItemRepository.deleteAll();
    }

    public void updateItem(Long itemId, String content) {
        Optional<FileItem> optionalItem = fileItemRepository.findById(itemId);
        if (optionalItem.isPresent()) {
            FileItem fileItem = optionalItem.get();
            fileItem.setContent(content);
            fileItemRepository.save(fileItem);
        } else {
            throw new RuntimeException("FileItem not found for updating content");
        }
    }
}
