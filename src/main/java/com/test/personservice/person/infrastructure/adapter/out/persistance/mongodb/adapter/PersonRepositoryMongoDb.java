package com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.adapter;

import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.config.MongoDBPersonRepositoryConfig;
import com.test.personservice.person.infrastructure.adapter.out.persistance.mongodb.mapper.MongoDBPersonEntityMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PersonRepositoryMongoDb {

  private final MongoDBPersonEntityMapper mapper;
  private final MongoDBPersonRepositoryConfig repository;

  public PersonRepositoryMongoDb(MongoDBPersonEntityMapper mapper,
      MongoDBPersonRepositoryConfig repository) {
    this.mapper = mapper;
    this.repository = repository;
  }

  public Mono<List<Person>> findAll() {
    return this.repository.findAll()
        .map(this.mapper::toDomain)
        .collectList();
  }

  public Mono<Person> findById(UUID id) {
    return this.repository.findById(id)
        .map(this.mapper::toDomain);
  }

  public Mono<Person> save(Person person) {
    return this.repository.save(this.mapper.toEntity(person))
        .map(this.mapper::toDomain);
  }

  public Mono<Void> deleteById(UUID id) {
    return this.repository.deleteById(id);
  }

}
