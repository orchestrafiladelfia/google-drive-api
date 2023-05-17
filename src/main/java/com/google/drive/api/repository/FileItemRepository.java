package com.google.drive.api.repository;

import com.google.drive.api.model.FileItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileItemRepository extends JpaRepository<FileItem, Long> {
    FileItem findByName(String name);
}
