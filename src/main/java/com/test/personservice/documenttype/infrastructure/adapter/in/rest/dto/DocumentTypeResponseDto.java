package com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record DocumentTypeResponseDto(UUID id, String name) {

}
