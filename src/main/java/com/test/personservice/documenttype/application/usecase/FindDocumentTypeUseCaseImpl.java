package com.test.personservice.documenttype.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class FindDocumentTypeUseCaseImpl implements FindDocumentTypeUseCase {

  private final DocumentTypeRepositoryOut documentTypeRepositoryOut;

  public FindDocumentTypeUseCaseImpl(DocumentTypeRepositoryOut documentTypeRepositoryOut) {
    this.documentTypeRepositoryOut = documentTypeRepositoryOut;
  }

  @Override
  public Mono<List<DocumentType>> findAll() {
    return this.documentTypeRepositoryOut.findAll();
  }

  @Override
  public Mono<DocumentType> findById(UUID id) {
    return this.documentTypeRepositoryOut.findById(id)
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", id))));
  }

}
