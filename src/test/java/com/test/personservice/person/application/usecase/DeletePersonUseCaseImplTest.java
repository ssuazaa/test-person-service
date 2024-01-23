package com.test.personservice.person.application.usecase;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.DeletePersonUseCase;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DeletePersonUseCaseImplTest {

  @MockBean
  FindPersonUseCase findPersonUseCase;

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  DeletePersonUseCase deletePersonUseCase;

  @BeforeEach
  void setUp() {
    this.findPersonUseCase = mock(FindPersonUseCaseImpl.class);
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.deletePersonUseCase = new DeletePersonUseCaseImpl(this.findPersonUseCase,
        this.personRepositoryOut);
  }

  @Test
  @DisplayName("testDelete() -> Good case")
  void testDelete() {
    // Arrange
    var personId = UUID.randomUUID();
    var personDb = Person.builder()
        .id(personId)
        .firstName("John")
        .lastName("Doe")
        .documentType(DocumentType.builder()
            .id(UUID.randomUUID())
            .name("DNI")
            .build())
        .build();

    when(this.findPersonUseCase.findById(any(UUID.class))).thenReturn(Mono.just(personDb));
    when(this.personRepositoryOut.deleteById(any(UUID.class))).thenReturn(Mono.empty());

    // Act
    var result = this.deletePersonUseCase.delete(personId);

    // Assert
    StepVerifier.create(result)
        .expectComplete()
        .verify();

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).deleteById(any(UUID.class));
  }

  @Test
  @DisplayName("testDelete() -> Bad case [Person not found]")
  void testDeletePersonNotFound() {
    // Arrange
    var personId = UUID.randomUUID();

    when(this.findPersonUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", personId))));

    // Act
    var result = this.deletePersonUseCase.delete(personId);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).deleteById(any(UUID.class));
  }

}
