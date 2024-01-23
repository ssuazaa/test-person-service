package com.test.personservice.documentType.application.usecase;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.application.usecase.DeleteDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.application.usecase.FindDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.DeleteDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class DeleteDocumentTypeUseCaseImplTest {

  @MockBean
  FindDocumentTypeUseCase findDocumentTypeUseCase;

  @MockBean
  DocumentTypeRepositoryOut documentTypeRepositoryOut;

  DeleteDocumentTypeUseCase deleteDocumentTypeUseCase;

  @BeforeEach
  void setUp() {
    this.findDocumentTypeUseCase = mock(FindDocumentTypeUseCaseImpl.class);
    this.documentTypeRepositoryOut = mock(DocumentTypeRepositoryOut.class);
    this.deleteDocumentTypeUseCase = new DeleteDocumentTypeUseCaseImpl(this.findDocumentTypeUseCase,
        this.documentTypeRepositoryOut);
  }

  @Test
  @DisplayName("testDelete() -> Good case")
  void testDelete() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentTypeDb = DocumentType.builder()
        .id(documentTypeId)
        .name("DNI")
        .build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class))).thenReturn(
        Mono.just(documentTypeDb));
    when(this.documentTypeRepositoryOut.deleteById(any(UUID.class))).thenReturn(Mono.empty());

    // Act
    var result = this.deleteDocumentTypeUseCase.delete(documentTypeId);

    // Assert
    StepVerifier.create(result)
        .expectComplete()
        .verify();

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.documentTypeRepositoryOut, times(1)).deleteById(any(UUID.class));
  }

  @Test
  @DisplayName("testDelete() -> Bad case [Document type not found]")
  void testDeleteDocumentTypeNotFound() {
    // Arrange
    var documentTypeId = UUID.randomUUID();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", documentTypeId))));

    // Act
    var result = this.deleteDocumentTypeUseCase.delete(documentTypeId);

    // Assert
    StepVerifier.create(result)
        .consumeErrorWith((Throwable error) ->
            assertInstanceOf(ObjectNotFoundException.class, error))
        .verify();

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.documentTypeRepositoryOut, times(0)).deleteById(any(UUID.class));
  }

}
