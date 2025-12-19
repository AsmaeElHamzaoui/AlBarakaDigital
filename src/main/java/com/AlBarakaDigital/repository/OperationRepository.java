package com.AlBarakaDigital.repository;

import com.AlBarakaDigital.entity.Operation;
import com.AlBarakaDigital.enums.OperationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByAccountSource_Owner_Email(String email);

    List<Operation> findByStatus(OperationStatus status);
}
