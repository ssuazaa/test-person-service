package com.test.personservice.documenttype.domain.port.in;

import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DeleteDocumentTypeUseCase {

  Mono<Void> delete(UUID id);

}
