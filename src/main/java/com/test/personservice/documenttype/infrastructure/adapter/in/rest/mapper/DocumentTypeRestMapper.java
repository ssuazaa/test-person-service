package com.test.personservice.documenttype.infrastructure.adapter.in.rest.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeRequestDto;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface DocumentTypeRestMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  DocumentType toDomain(DocumentTypeRequestDto documentTypeRequestDto);

  DocumentTypeResponseDto toResponse(DocumentType documentType);

}

