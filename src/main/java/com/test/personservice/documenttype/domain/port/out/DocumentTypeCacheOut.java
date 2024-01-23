package com.test.personservice.documenttype.domain.port.out;

import com.test.personservice.documenttype.domain.model.DocumentType;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface DocumentTypeCacheOut {

  Mono<DocumentType> findByKey(UUID key);

  Mono<Void> save(UUID key, DocumentType documentType);

  Mono<Void> deleteByKey(UUID key);

}
