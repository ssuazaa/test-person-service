package com.test.personservice.documentType.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.application.usecase.CreateDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.CreateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class CreateDocumentTypeUseCaseImplTest {

  @MockBean
  DocumentTypeRepositoryOut documentTypeRepositoryOut;

  CreateDocumentTypeUseCase createDocumentTypeUseCase;

  @BeforeEach
  void setUp() {
    this.documentTypeRepositoryOut = mock(DocumentTypeRepositoryOut.class);
    this.createDocumentTypeUseCase = new CreateDocumentTypeUseCaseImpl(
        this.documentTypeRepositoryOut);
  }

  @Test
  @DisplayName("testCreate() -> Good case")
  void testCreate() {
    // Arrange
    var documentType = DocumentType.builder()
        .name("DNI")
        .build();
    var documentTypeSaved = documentType.toBuilder()
        .id(UUID.randomUUID())
        .build();

    when(this.documentTypeRepositoryOut.save(any(DocumentType.class)))
        .thenReturn(Mono.just(documentTypeSaved));

    // Act
    var result = this.createDocumentTypeUseCase.create(documentType);

    // Assert
    StepVerifier.create(result)
        .expectNextMatches(Objects::nonNull)
        .verifyComplete();

    verify(this.documentTypeRepositoryOut, times(1)).save(any(DocumentType.class));
  }

}
