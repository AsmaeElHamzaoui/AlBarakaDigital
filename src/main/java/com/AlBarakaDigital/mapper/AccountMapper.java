package com.AlBarakaDigital.mapper;

import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "owner.fullName", target = "owner")
    AccountResponseDTO toDto(Account account);
}

