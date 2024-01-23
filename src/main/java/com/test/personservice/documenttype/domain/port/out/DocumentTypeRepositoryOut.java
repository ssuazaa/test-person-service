package com.test.personservice.documenttype.domain.port.out;

import com.test.personservice.documenttype.domain.model.DocumentType;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DocumentTypeRepositoryOut {

  Mono<List<DocumentType>> findAll();

  Mono<DocumentType> findById(UUID id);

  Mono<DocumentType> save(DocumentType documentType);

  Mono<Void> deleteById(UUID id);

}
