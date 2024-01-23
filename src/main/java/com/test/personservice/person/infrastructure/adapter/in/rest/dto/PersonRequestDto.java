package com.test.personservice.person.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PersonRequestDto(
    @NotBlank(message = "FirstName is required and cannot be empty") String firstName,
    @NotBlank(message = "LastName is required and cannot be empty") String lastName,
    @NotNull(message = "DocumentType is required and cannot be null") PersonDocumentTypeDto documentType) {

}
