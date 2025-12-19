package com.AlBarakaDigital.mapper;

import com.AlBarakaDigital.dto.DocumentResponseDTO;
import org.mapstruct.Mapper;

import javax.swing.text.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {

    DocumentResponseDTO toDto(Document document);
}
