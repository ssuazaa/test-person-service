package com.test.personservice.documentType.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.application.usecase.FindDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.application.usecase.UpdateDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.UpdateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class UpdateDocumentTypeUseCaseImplTest {

  @MockBean
  FindDocumentTypeUseCase findDocumentTypeUseCase;

  @MockBean
  DocumentTypeRepositoryOut documentTypeRepositoryOut;

  UpdateDocumentTypeUseCase updateDocumentTypeUseCase;

  @BeforeEach
  void setUp() {
    this.findDocumentTypeUseCase = mock(FindDocumentTypeUseCaseImpl.class);
    this.documentTypeRepositoryOut = mock(DocumentTypeRepositoryOut.class);
    this.updateDocumentTypeUseCase = new UpdateDocumentTypeUseCaseImpl(this.findDocumentTypeUseCase,
        this.documentTypeRepositoryOut);
  }

  @Test
  @DisplayName("testUpdate() -> Good case")
  void testUpdate() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentTypeRequest = DocumentType.builder()
        .name("NIE")
        .build();
    var documentTypeDb = DocumentType.builder()
        .id(documentTypeId)
        .name("DNI")
        .build();
    var documentTypeUpdated = documentTypeRequest.toBuilder()
        .id(documentTypeId)
        .build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class))).thenReturn(
        Mono.just(documentTypeDb));
    when(this.documentTypeRepositoryOut.save(any(DocumentType.class))).thenReturn(
        Mono.just(documentTypeUpdated));

    // Act
    var result = this.updateDocumentTypeUseCase.update(documentTypeId, documentTypeRequest);

    // Assert
    StepVerifier.create(result)
        .expectComplete()
        .verify();

    assertNotNull(result);
    assertThat(documentTypeUpdated.id()).isEqualTo(documentTypeId);
    assertThat(documentTypeUpdated.name()).isEqualTo(documentTypeRequest.name());

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.documentTypeRepositoryOut, times(1)).save(any(DocumentType.class));
  }

  @Test
  @DisplayName("testUpdate() -> Bad case [document type not found]")
  void testUpdateDocumentTypeNotFound() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentTypeRequest = DocumentType.builder().build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", documentTypeId))));

    // Act
    var result = this.updateDocumentTypeUseCase.update(documentTypeId, documentTypeRequest);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.documentTypeRepositoryOut, times(0)).save(any(DocumentType.class));
  }

}
