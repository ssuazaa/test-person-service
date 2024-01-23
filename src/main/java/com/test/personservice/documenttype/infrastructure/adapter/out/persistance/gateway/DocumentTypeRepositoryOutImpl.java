package com.test.personservice.documenttype.infrastructure.adapter.out.persistance.gateway;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.adapter.DocumentTypeRepositoryMongoDb;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DocumentTypeRepositoryOutImpl implements DocumentTypeRepositoryOut {

  private final DocumentTypeRepositoryMongoDb repository;

  public DocumentTypeRepositoryOutImpl(DocumentTypeRepositoryMongoDb repository) {
    this.repository = repository;
  }

  @Override
  public Mono<List<DocumentType>> findAll() {
    return this.repository.findAll();
  }

  @Override
  public Mono<DocumentType> findById(UUID id) {
    return this.repository.findById(id);
  }

  @Override
  public Mono<DocumentType> save(DocumentType documentType) {
    return this.repository.save(documentType);
  }

  @Override
  public Mono<Void> deleteById(UUID id) {
    return this.repository.deleteById(id);
  }

}
