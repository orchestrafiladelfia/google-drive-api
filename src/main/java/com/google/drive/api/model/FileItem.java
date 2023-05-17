package com.google.drive.api.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class FileItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fileId;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String content;
}
