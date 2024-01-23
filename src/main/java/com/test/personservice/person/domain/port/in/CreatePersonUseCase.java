package com.test.personservice.person.domain.port.in;

import com.test.personservice.person.domain.model.Person;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface CreatePersonUseCase {

  Mono<UUID> create(Person person);

}
