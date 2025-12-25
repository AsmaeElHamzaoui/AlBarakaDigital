package com.AlBarakaDigital.repository;

import com.AlBarakaDigital.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByOperation_Id(Long operationId);
}
