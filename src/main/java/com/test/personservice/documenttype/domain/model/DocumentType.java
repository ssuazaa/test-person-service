package com.test.personservice.documenttype.domain.model;

import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record DocumentType(UUID id,
                           String name,
                           Long version) {

}
