package com.test.personservice.documentType.infrastructure.adapter.in.rest.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.CreateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.DeleteDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.UpdateDocumentTypeUseCase;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.adapter.DocumentTypeRestController;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeRequestDto;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeResponseDto;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.mapper.DocumentTypeRestMapper;
import com.test.personservice.shared.infrastructure.config.errorhandler.ErrorResponseDto;
import com.test.personservice.shared.infrastructure.config.exceptions.ObjectNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(DocumentTypeRestController.class)
class DocumentTypeRestControllerTest {

  @MockBean
  private FindDocumentTypeUseCase findDocumentTypeUseCase;

  @MockBean
  private CreateDocumentTypeUseCase createDocumentTypeUseCase;

  @MockBean
  private UpdateDocumentTypeUseCase updateDocumentTypeUseCase;

  @MockBean
  private DeleteDocumentTypeUseCase deleteDocumentTypeUseCase;

  @MockBean
  private DocumentTypeRestMapper documentTypeRestMapper;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    when(this.documentTypeRestMapper.toResponse(any(DocumentType.class)))
        .thenAnswer(invocation -> {
          DocumentType documentType = invocation.getArgument(0);
          return DocumentTypeResponseDto.builder()
              .id(documentType.id())
              .name(documentType.name())
              .build();
        });
    when(this.documentTypeRestMapper.toDomain(any(DocumentTypeRequestDto.class)))
        .thenAnswer(invocation -> {
          DocumentTypeRequestDto documentTypeRequestDto = invocation.getArgument(0);
          return DocumentType.builder()
              .name(documentTypeRequestDto.name())
              .build();
        });
  }

  @Test
  @DisplayName("testFindAll() -> Good case [not empty]")
  void testFindAll() {
    // Arrange
    var documentTypes = List.of(DocumentType.builder()
        .id(UUID.randomUUID())
        .name("DNI")
        .build(), DocumentType.builder()
        .id(UUID.randomUUID())
        .name("NIE")
        .build());

    when(this.findDocumentTypeUseCase.findAll()).thenReturn(Mono.just(documentTypes));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/document-types")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(DocumentTypeResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertFalse(response.isEmpty());
    assertAll("Validate each element", () -> {
      for (var i = 0; i < documentTypes.size(); i++) {
        assertEquals(response.get(i).id(), documentTypes.get(i).id());
        assertEquals(response.get(i).name(), documentTypes.get(i).name());
      }
    });

    verify(this.findDocumentTypeUseCase, times(1)).findAll();
    verify(this.documentTypeRestMapper, times(documentTypes.size()))
        .toResponse(any(DocumentType.class));
  }

  @Test
  @DisplayName("testFindAll() -> Good case [empty]")
  void testFindAllEmpty() {
    // Arrange
    when(this.findDocumentTypeUseCase.findAll()).thenReturn(Mono.just(Collections.emptyList()));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/document-types")
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBodyList(DocumentTypeResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertTrue(response.isEmpty());

    verify(this.findDocumentTypeUseCase, times(1)).findAll();
    verify(this.documentTypeRestMapper, times(0)).toResponse(any(DocumentType.class));
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
    var documentTypeResponseDto = DocumentTypeResponseDto.builder()
        .id(documentTypeId)
        .name(documentTypeSaved.name())
        .build();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.just(documentTypeSaved));
    when(this.documentTypeRestMapper.toResponse(any(DocumentType.class)))
        .thenReturn(documentTypeResponseDto);

    // Act
    var response = webTestClient.get().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isOk()
        .expectBody(DocumentTypeResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(documentTypeSaved.id()).isEqualTo(response.id());
    assertThat(documentTypeSaved.name()).isEqualTo(response.name());

    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
    verify(this.documentTypeRestMapper, times(1)).toResponse(any(DocumentType.class));
  }

  @Test
  @DisplayName("testFindById() -> Bad case [not found]")
  void testFindByIdObjectNotFoundException() {
    // Arrange
    var documentTypeId = UUID.randomUUID();

    when(this.findDocumentTypeUseCase.findById(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", documentTypeId))));

    // Act
    var response = this.webTestClient.get().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");

    verify(this.documentTypeRestMapper, times(0)).toResponse(any(DocumentType.class));
    verify(this.findDocumentTypeUseCase, times(1)).findById(any(UUID.class));
  }

  @Test
  @DisplayName("testCreate()")
  void testCreate() {
    // Arrange
    var documentTypeIdCreated = UUID.randomUUID();
    var documentType = DocumentType.builder()
        .name("DNI")
        .build();
    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
        .name("DNI")
        .build();

    when(this.documentTypeRestMapper.toDomain(any(DocumentTypeRequestDto.class)))
        .thenReturn(documentType);
    when(this.createDocumentTypeUseCase.create(any(DocumentType.class)))
        .thenReturn(Mono.just(documentTypeIdCreated));

    // Act
    var response = webTestClient.post().uri("/api/v1/document-types")
        .header(HttpHeaders.ACCEPT, "application/json")
        .bodyValue(documentTypeRequestDto)
        .exchange()
        .expectStatus().isCreated()
        .returnResult(String.class)
        .getResponseHeaders().getFirst(HttpHeaders.LOCATION);

    // Assert
    assertNotNull(response);
    assertThat(response).isEqualTo("/api/v1/document-types/" + documentTypeIdCreated);

    verify(this.documentTypeRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
    verify(this.createDocumentTypeUseCase, times(1)).create(any(DocumentType.class));
  }

  @Test
  @DisplayName("testUpdate() -> Good case")
  void testUpdate() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentType = DocumentType.builder()
        .name("DNI")
        .build();
    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
        .name("DNI")
        .build();

    when(this.documentTypeRestMapper.toDomain(any(DocumentTypeRequestDto.class)))
        .thenReturn(documentType);
    when(this.updateDocumentTypeUseCase.update(any(UUID.class), any(DocumentType.class)))
        .thenReturn(Mono.empty());

    // Act
    webTestClient.put().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .bodyValue(documentTypeRequestDto)
        .exchange()
        .expectStatus().isNoContent();

    // Assert
    verify(this.documentTypeRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
    verify(this.updateDocumentTypeUseCase, times(1)).update(any(UUID.class),
        any(DocumentType.class));
  }

  @Test
  @DisplayName("testUpdate() -> Bad case [not found]")
  void testUpdateObjectNotFoundException() {
    // Arrange
    var documentTypeId = UUID.randomUUID();
    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
        .name("DNI")
        .build();

    when(this.updateDocumentTypeUseCase.update(any(UUID.class), any(DocumentType.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", documentTypeId))));

    // Act
    var response = this.webTestClient.put().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .bodyValue(documentTypeRequestDto)
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");

    verify(this.documentTypeRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
    verify(this.updateDocumentTypeUseCase, times(1)).update(any(UUID.class),
        any(DocumentType.class));
  }

  @Test
  @DisplayName("testDelete() -> Good case")
  void testDelete() {
    // Arrange
    var documentTypeId = UUID.randomUUID();

    when(this.deleteDocumentTypeUseCase.delete(any(UUID.class)))
        .thenReturn(Mono.empty());

    // Act
    webTestClient.delete().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isNoContent();

    // Assert
    verify(this.deleteDocumentTypeUseCase, times(1)).delete(any(UUID.class));
  }

  @Test
  @DisplayName("testDelete() -> Bad case [not found]")
  void testDeleteObjectNotFoundException() {
    // Arrange
    var documentTypeId = UUID.randomUUID();

    when(this.deleteDocumentTypeUseCase.delete(any(UUID.class)))
        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
            String.format("Document type with id '%s' was not found", documentTypeId))));

    // Act
    var response = this.webTestClient.delete().uri("/api/v1/document-types/{id}", documentTypeId)
        .header(HttpHeaders.ACCEPT, "application/json")
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorResponseDto.class)
        .returnResult()
        .getResponseBody();

    // Assert
    assertNotNull(response);
    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");

    verify(this.deleteDocumentTypeUseCase, times(1)).delete(any(UUID.class));
  }

}
