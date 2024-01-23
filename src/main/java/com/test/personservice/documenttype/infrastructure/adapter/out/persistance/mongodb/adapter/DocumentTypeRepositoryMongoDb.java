package com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.adapter;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.config.MongoDBDocumentTypeRepositoryConfig;
import com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.mapper.MongoDBDocumentTypeEntityMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class DocumentTypeRepositoryMongoDb {

  private final MongoDBDocumentTypeEntityMapper mapper;
  private final MongoDBDocumentTypeRepositoryConfig repository;

  public DocumentTypeRepositoryMongoDb(MongoDBDocumentTypeEntityMapper mapper,
      MongoDBDocumentTypeRepositoryConfig repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  public Mono<List<DocumentType>> findAll() {
    return this.repository.findAll()
        .map(this.mapper::toDomain)
        .collectList();
  }

  public Mono<DocumentType> findById(UUID id) {
    return this.repository.findById(id)
        .map(this.mapper::toDomain);
  }

  public Mono<DocumentType> save(DocumentType documentType) {
    return this.repository.save(this.mapper.toEntity(documentType))
        .map(this.mapper::toDomain);
  }

  public Mono<Void> deleteById(UUID id) {
    return this.repository.deleteById(id);
  }

}
