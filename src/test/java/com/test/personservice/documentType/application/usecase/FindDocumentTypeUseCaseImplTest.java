package com.test.personservice.documentType.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.BaseException;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class FindDocumentTypeUseCaseImplTest {

  @MockBean
  DocumentTypeRepositoryOut documentTypeRepositoryOut;

  FindDocumentTypeUseCase findDocumentTypeUseCase;

  @BeforeEach
  void setUp() {
    this.documentTypeRepositoryOut = mock(DocumentTypeRepositoryOut.class);
    this.findDocumentTypeUseCase = new FindDocumentTypeUseCaseImpl(this.documentTypeRepositoryOut);
  }

  @Test
  @DisplayName("testFindAll() -> Good case")
  void testFindAll() {
    // Arrange
    var documentTypesSaved = List.of(DocumentType.builder()
        .id(UUID.randomUUID())
        .name("DNI")
        .build(), DocumentType.builder()
        .id(UUID.randomUUID())
        .name("NIE")
        .build());

    when(this.documentTypeRepositoryOut.findAll()).thenReturn(Mono.just(documentTypesSaved));

    // Act
    var result = this.findDocumentTypeUseCase.findAll();

    // Assert
    StepVerifier.create(result)
        .consumeNextWith((List<DocumentType> documentTypes) ->
            assertThat(documentTypes).hasSize(documentTypesSaved.size()))
        .expectComplete()
        .verify();

    verify(this.documentTypeRepositoryOut, times(1)).findAll();
  }

  @Test
  @DisplayName("testFindAll() -> Empty case")
  void testFindAllEmptyCase() {
    // Arrange
    List<DocumentType> documentTypesSaved = Collections.emptyList();

    when(this.documentTypeRepositoryOut.findAll()).thenReturn(Mono.just(documentTypesSaved));

    // Act
    var result = this.findDocumentTypeUseCase.findAll();

    // Assert
    StepVerifier.create(result)
        .consumeNextWith((List<DocumentType> documentTypes) -> assertThat(documentTypes).isEmpty())
        .expectComplete()
        .verify();

    verify(this.documentTypeRepositoryOut, times(1)).findAll();
  }

  @Test
  @DisplayName("testFindById() -> Good case")
  void testFindById() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentTypeSaved = DocumentType.builder()
        .id(documentTypeId)
        .name("DNI")
        .build();

    when(this.documentTypeRepositoryOut.findById(any(UUID.class))).thenReturn(
        Mono.just(documentTypeSaved));

    // Act
    var result = this.findDocumentTypeUseCase.findById(documentTypeId);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    assertNotNull(result);
    assertThat(documentTypeSaved.id()).isEqualTo(documentTypeId);
    assertThat(documentTypeSaved.name()).isEqualTo("DNI");

    verify(this.documentTypeRepositoryOut, times(1)).findById(any(UUID.class));
  }

  @Test
  @DisplayName("testFindById() -> Bad case [document type not found]")
  void testFindByIdDocumentTypeNotFound() {
    // Arrange
    var documentTypeId = UUID.randomUUID();

    when(this.documentTypeRepositoryOut.findById(any(UUID.class))).thenReturn(Mono.empty());

    // Act
    var result = this.findDocumentTypeUseCase.findById(documentTypeId);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) -> {
          assertInstanceOf(ObjectNotFoundException.class, error);
          assertEquals("DOCUMENT_TYPE_NOT_FOUND", ((BaseException) error).getKey());
          assertEquals(404, ((BaseException) error).getStatusCode());
          assertEquals(String.format("Document type with id '%s' was not found", documentTypeId),
              error.getMessage());
        })
        .verify();

    verify(this.documentTypeRepositoryOut, times(1)).findById(any(UUID.class));
  }

}
