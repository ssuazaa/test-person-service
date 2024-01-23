package com.test.personservice.documenttype.domain.port.in;

import com.test.personservice.documenttype.domain.model.DocumentType;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface FindDocumentTypeUseCase {

  Mono<List<DocumentType>> findAll();

  Mono<DocumentType> findById(UUID id);

}
