package com.test.personservice.documenttype.domain.port.in;

import com.test.personservice.documenttype.domain.model.DocumentType;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface UpdateDocumentTypeUseCase {

  Mono<Void> update(UUID id, DocumentType documentType);

}
