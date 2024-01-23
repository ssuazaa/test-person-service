package com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.entity.DocumentTypeEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface MongoDBDocumentTypeEntityMapper {

  DocumentType toDomain(DocumentTypeEntity documentTypeEntity);

  @InheritInverseConfiguration
  DocumentTypeEntity toEntity(DocumentType documentType);

}

