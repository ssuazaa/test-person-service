package com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DocumentTypeRequestDto(
    @NotBlank(message = "Name is required and cannot be empty") String name) {

}
