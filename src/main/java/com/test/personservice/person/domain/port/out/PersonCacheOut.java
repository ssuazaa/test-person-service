package com.test.personservice.person.domain.port.out;

import com.test.personservice.person.domain.model.Person;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface PersonCacheOut {

  Mono<Person> findByKey(UUID key);

  Mono<Void> save(UUID key, Person person);

  Mono<Void> deleteByKey(UUID key);

}
