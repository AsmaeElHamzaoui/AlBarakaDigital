package com.AlBarakaDigital.mapper;

import com.AlBarakaDigital.dto.AccountRequestDTO;
import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {UserMapper.class, OperationMapper.class}
)
public interface AccountMapper {


    // RequestDTO → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "outgoingOperations", ignore = true)
    @Mapping(target = "incomingOperations", ignore = true)
    Account toEntity(AccountRequestDTO dto);


    // Entity → ResponseDTO
    AccountResponseDTO toDto(Account account);
}
