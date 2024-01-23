package com.test.personservice.person.domain.model;

import com.test.personservice.documenttype.domain.model.DocumentType;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Person(UUID id,
                     String firstName,
                     String lastName,
                     DocumentType documentType,
                     Long version) {

}
