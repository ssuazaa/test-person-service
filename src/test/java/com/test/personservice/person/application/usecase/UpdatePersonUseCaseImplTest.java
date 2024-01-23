package com.test.personservice.person.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.application.usecase.FindDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdatePersonUseCaseImplTest {

  @MockBean
  FindDocumentTypeUseCase findDocumentTypeUseCase;

  @MockBean
  FindPersonUseCase findPersonUseCase;

  @MockBean
  PersonRepositoryOut personRepositoryOut;

  UpdatePersonUseCase updatePersonUseCase;

  @BeforeEach
  void setUp() {
    this.findDocumentTypeUseCase = mock(FindDocumentTypeUseCaseImpl.class);
    this.findPersonUseCase = mock(FindPersonUseCaseImpl.class);
    this.personRepositoryOut = mock(PersonRepositoryOut.class);
    this.updatePersonUseCase = new UpdatePersonUseCaseImpl(this.findDocumentTypeUseCase,
        this.findPersonUseCase, this.personRepositoryOut);
  }

  @Test
  @DisplayName("testUpdate() -> Good case")
  void testUpdate() {
    // Arrange
    var personId = UUID.randomUUID();
    var personRequest = Person.builder()
        .firstName("John")
        .lastName("Doe")
        .documentType(DocumentType.builder()
            .id(UUID.randomUUID())
            .name("DNI")
            .build())
        .build();
    var personDb = Person.builder()
        .id(personId)
        .firstName(personRequest.firstName())
        .lastName(personRequest.lastName())
        .documentType(DocumentType.builder()
            .id(personRequest.documentType().id())
            .name(personRequest.documentType().name())
            .build())
        .build();
    var personUpdated = personRequest.toBuilder()
        .id(personId)
        .build();
    var documentType = DocumentType.builder()
        .id(personRequest.documentType().id())
        .name(personRequest.documentType().name())
        .build();

    when(this.findPersonUseCase.findById(any(UUID.class))).thenReturn(Mono.just(personDb));
    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.just(documentType));
    when(this.personRepositoryOut.save(any(Person.class))).thenReturn(Mono.just(personUpdated));

    // Act
    var result = this.updatePersonUseCase.update(personId, personRequest);

    // Assert
    StepVerifier.create(result)
        .expectComplete()
        .verify();

    assertNotNull(result);
    assertThat(personUpdated.id()).isEqualTo(personId);
    assertThat(personUpdated.firstName()).isEqualTo(personRequest.firstName());
    assertThat(personUpdated.lastName()).isEqualTo(personRequest.lastName());

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(1)).save(any(Person.class));
  }

  @Test
  @DisplayName("testUpdate() -> Bad case [person not found]")
  void testUpdatePersonNotFound() {
    // Arrange
    var personId = UUID.randomUUID();
    var personRequest = Person.builder().build();

    when(this.findPersonUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("PERSON_NOT_FOUND",
            String.format("Person with id '%s' was not found", personId))));

    // Act
    var result = this.updatePersonUseCase.update(personId, personRequest);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
    verify(this.findDocumentTypeUseCase, times(0)).findById(any(UUID.class));
    verify(this.personRepositoryOut, times(0)).save(any(Person.class));
  }

}
