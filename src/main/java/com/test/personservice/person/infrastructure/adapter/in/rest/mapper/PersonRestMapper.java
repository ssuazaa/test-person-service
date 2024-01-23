package com.test.personservice.person.infrastructure.adapter.in.rest.mapper;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonRequestDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface PersonRestMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "version", ignore = true)
  Person toDomain(PersonRequestDto personRequestDto);

  PersonResponseDto toResponse(Person person);

}

