package com.test.personservice.documenttype.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.UpdateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class UpdateDocumentTypeUseCaseImpl implements UpdateDocumentTypeUseCase {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final DocumentTypeRepositoryOut documentTypeRepositoryOut;

  public UpdateDocumentTypeUseCaseImpl(FindDocumentTypeUseCase findDocumentTypeUseCase,
      DocumentTypeRepositoryOut documentTypeRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.documentTypeRepositoryOut = documentTypeRepositoryOut;
  }

  @Override
  public Mono<Void> update(UUID id, DocumentType documentType) {
    return this.findDocumentTypeUseCase.findById(id)
        .map((DocumentType documentTypeDB) -> documentTypeDB.toBuilder()
            .name(documentType.name())
            .build())
        .flatMap(this.documentTypeRepositoryOut::save)
        .then();
  }

}
