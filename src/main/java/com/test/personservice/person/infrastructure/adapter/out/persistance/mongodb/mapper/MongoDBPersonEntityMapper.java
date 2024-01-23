package com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.entity.PersonEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface MongoDBPersonEntityMapper {

  @Mapping(source = "documentTypeId", target = "documentType.id")
  Person toDomain(PersonEntity personEntity);

  @InheritInverseConfiguration
  PersonEntity toEntity(Person person);

}

