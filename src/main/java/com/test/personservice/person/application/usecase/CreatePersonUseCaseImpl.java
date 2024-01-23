package com.test.personservice.person.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.CreatePersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class CreatePersonUseCaseImpl implements CreatePersonUseCase {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public CreatePersonUseCaseImpl(FindDocumentTypeUseCase findDocumentTypeUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<UUID> create(Person person) {
    return validateDocumentType(person.documentType())
        .then(Mono.just(person.toBuilder()
                .id(UUID.randomUUID())
                .build())
            .flatMap(this.personRepositoryOut::save)
            .map(Person::id));
  }

  public Mono<Void> validateDocumentType(DocumentType documentType) {
    return this.findDocumentTypeUseCase.findById(documentType.id())
        .then();
  }

}
