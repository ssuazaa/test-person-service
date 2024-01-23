package com.test.personservice.person.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class UpdatePersonUseCaseImpl implements UpdatePersonUseCase {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final FindPersonUseCase findPersonUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public UpdatePersonUseCaseImpl(FindDocumentTypeUseCase findDocumentTypeUseCase,
      FindPersonUseCase findPersonUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.findPersonUseCase = findPersonUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<Void> update(UUID id, Person person) {
    return this.findPersonUseCase.findById(id)
        .flatMap((Person personDb) -> validateDocumentType(person.documentType())
            .then(Mono.just(personDb)))
        .map((Person personDb) -> personDb.toBuilder()
            .firstName(person.firstName())
            .lastName(person.lastName())
            .documentType(person.documentType())
            .build())
        .flatMap(this.personRepositoryOut::save)
        .then();
  }

  public Mono<Void> validateDocumentType(DocumentType documentType) {
    return this.findDocumentTypeUseCase.findById(documentType.id())
        .then();
  }

}
