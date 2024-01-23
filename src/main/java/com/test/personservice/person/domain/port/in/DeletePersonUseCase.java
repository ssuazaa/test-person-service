package com.test.personservice.person.domain.port.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeletePersonUseCase {

  Mono<Void> delete(UUID id);

}
