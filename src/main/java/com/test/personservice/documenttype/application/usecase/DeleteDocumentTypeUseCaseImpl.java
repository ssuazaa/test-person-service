package com.test.personservice.documenttype.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.DeleteDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class DeleteDocumentTypeUseCaseImpl implements DeleteDocumentTypeUseCase {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final DocumentTypeRepositoryOut documentTypeRepositoryOut;

  public DeleteDocumentTypeUseCaseImpl(FindDocumentTypeUseCase findDocumentTypeUseCase,
      DocumentTypeRepositoryOut documentTypeRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.documentTypeRepositoryOut = documentTypeRepositoryOut;
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return this.findDocumentTypeUseCase.findById(id)
        .map(DocumentType::id)
        .flatMap(this.documentTypeRepositoryOut::deleteById)
        .then();
  }

}
