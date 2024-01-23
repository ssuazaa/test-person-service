package com.test.personservice.person.application.usecase;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.application.usecase.FindDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.CreatePersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreatePersonUseCaseImplTest {

  @MockBean
  FindDocumentTypeUseCase findDocumentTypeUseCase;

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  CreatePersonUseCase createPersonUseCase;

  @BeforeEach
  void setUp() {
    this.findDocumentTypeUseCase = mock(FindDocumentTypeUseCaseImpl.class);
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.createPersonUseCase = new CreatePersonUseCaseImpl(this.findDocumentTypeUseCase,
        this.personRepositoryOut);
  }

  @Test
  @DisplayName("testCreate() -> Good case")
  void testCreate() {
    // Arrange
    var person = Person.builder()
        .firstName("John")
        .lastName("Doe")
        .documentType(DocumentType.builder()
            .id(UUID.randomUUID())
            .name("DNI")
            .build())
        .build();
    var personSaved = person.toBuilder()
        .id(UUID.randomUUID())
        .build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.empty());
    when(this.personRepositoryOut.save(any(Person.class)))
        .thenReturn(Mono.just(personSaved));

    // Act
    var result = this.createPersonUseCase.create(person);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).save(any(Person.class));
  }

  @Test
  @DisplayName("testCreate() -> Bad case [document type not found]")
  void testCreateDocumentTypeNotFound() {
    // Arrange
    var person = Person.builder()
        .firstName("John")
        .lastName("Doe")
        .documentType(DocumentType.builder()
            .id(UUID.randomUUID())
            .name("DNI")
            .build())
        .build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found",
                person.documentType().id()))));

    // Act
    var result = this.createPersonUseCase.create(person);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

}
