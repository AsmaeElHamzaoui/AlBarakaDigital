package com.AlBarakaDigital.mapper;

import com.AlBarakaDigital.dto.OperationResponseDTO;
import com.AlBarakaDigital.entity.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OperationMapper {

    @Mapping(source = "accountSource.accountNumber", target = "accountSource")
    @Mapping(source = "accountDestination.accountNumber", target = "accountDestination")
    OperationResponseDTO toDto(Operation operation);
}

