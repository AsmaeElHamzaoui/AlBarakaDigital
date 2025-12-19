package com.AlBarakaDigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private String storagePath;

    private LocalDateTime uploadedAt;

    @OneToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

    @PrePersist
    void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }
}
