package com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.config;

import com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.entity.PersonEntity;
import java.util.UUID;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MongoDBPersonRepositoryConfig extends ReactiveMongoRepository<PersonEntity, UUID> {

}
