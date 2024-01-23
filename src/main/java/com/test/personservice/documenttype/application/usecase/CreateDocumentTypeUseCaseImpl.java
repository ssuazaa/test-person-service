package com.test.personservice.documenttype.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.CreateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class CreateDocumentTypeUseCaseImpl implements CreateDocumentTypeUseCase {

  private final DocumentTypeRepositoryOut documentTypeRepositoryOut;

  public CreateDocumentTypeUseCaseImpl(DocumentTypeRepositoryOut documentTypeRepositoryOut) {
    this.documentTypeRepositoryOut = documentTypeRepositoryOut;
  }

  @Override
  public Mono<UUID> create(DocumentType documentType) {
    return Mono.just(documentType.toBuilder()
            .id(UUID.randomUUID())
            .build())
        .flatMap(this.documentTypeRepositoryOut::save)
        .map(DocumentType::id);
  }

}
