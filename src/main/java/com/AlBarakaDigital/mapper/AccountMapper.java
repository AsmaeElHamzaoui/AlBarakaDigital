package com.AlBarakaDigital.mapper;

import com.AlBarakaDigital.dto.AccountResponseDTO;
import com.AlBarakaDigital.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountResponseDTO toDto(Account account);
}

