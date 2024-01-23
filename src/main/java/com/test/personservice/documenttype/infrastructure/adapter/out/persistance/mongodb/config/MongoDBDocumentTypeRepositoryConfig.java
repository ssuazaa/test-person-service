package com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.config;

import com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.entity.DocumentTypeEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDBDocumentTypeRepositoryConfig extends ReactiveMongoRepository<DocumentTypeEntity, UUID> {

}
