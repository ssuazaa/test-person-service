package com.test.personservice.person.application.usecase;

import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.DeletePersonUseCase;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import java.util.UUID;
import reactor.core.publisher.Mono;

public class DeletePersonUseCaseImpl implements DeletePersonUseCase {

  private final FindPersonUseCase findPersonUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public DeletePersonUseCaseImpl(FindPersonUseCase findPersonUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findPersonUseCase = findPersonUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Override
  public Mono<Void> delete(UUID id) {
    return this.findPersonUseCase.findById(id)
        .map(Person::id)
        .flatMap(this.personRepositoryOut::deleteById)
        .then();
  }

}
