package com.test.personservice.person.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.Builder;

@Builder
public record PersonDocumentTypeDto(
    @NotBlank(message = "LastName is required and cannot be empty") UUID id,
    String name) {

}
