package com.test.personservice.documenttype.infrastructure.adapter.out.persistance.mongodb.entity;

import java.util.UUID;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "document_types")
public record DocumentTypeEntity(@MongoId UUID id,
                                 String name,
                                 @Version Long version) {

}
