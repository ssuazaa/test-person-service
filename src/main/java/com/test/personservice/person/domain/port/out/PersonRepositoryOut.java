package com.test.personservice.person.domain.port.out;

import com.test.personservice.person.domain.model.Person;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface PersonRepositoryOut {

  Mono<List<Person>> findAll();

  Mono<Person> findById(UUID id);

  Mono<Person> save(Person person);

  Mono<Void> deleteById(UUID id);

}
