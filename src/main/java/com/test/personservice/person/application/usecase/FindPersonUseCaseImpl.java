package com.test.personservice.person.application.usecase;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class FindPersonUseCaseImpl implements FindPersonUseCase {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public FindPersonUseCaseImpl(FindDocumentTypeUseCase findDocumentTypeUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<List<Person>> findAll() {
    return this.personRepositoryOut.findAll()
        .flatMapIterable(persons -> persons)
        .flatMap(this::completeData)
        .collectList();
  }

  @Override
  public Mono<Person> findById(UUID id) {
    return this.personRepositoryOut.findById(id)
        .switchIfEmpty(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", id))))
        .flatMap(this::completeData);
  }

  public Mono<Person> completeData(Person person) {
    return this.findDocumentTypeUseCase.findById(person.documentType().id())
        .map((DocumentType documentType) -> person.toBuilder()
            .documentType(documentType)
            .build())
        .onErrorReturn(person);
  }

}
