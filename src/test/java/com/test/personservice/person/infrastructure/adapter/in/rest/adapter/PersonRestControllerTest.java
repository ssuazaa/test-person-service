package com.test.personservice.person.infrastructure.adapter.in.rest.adapter;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeResponseDto;
import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.CreatePersonUseCase;
import com.test.personservice.person.domain.port.in.DeletePersonUseCase;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonDocumentTypeDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonRequestDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonResponseDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.mapper.PersonRestMapper;
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
@WebFluxTest(PersonRestController.class)
class PersonRestControllerTest {

  @MockBean
  private FindPersonUseCase findPersonUseCase;

  @MockBean
  private CreatePersonUseCase createPersonUseCase;

  @MockBean
  private UpdatePersonUseCase updatePersonUseCase;

  @MockBean
  private DeletePersonUseCase deletePersonUseCase;

  @MockBean
  private PersonRestMapper personRestMapper;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    when(this.personRestMapper.toResponse(any(Person.class)))
        .thenAnswer(invocation -> {
          Person person = invocation.getArgument(0);
          return PersonResponseDto.builder()
              .id(person.id())
              .firstName(person.firstName())
              .lastName(person.lastName())
              .documentType(PersonDocumentTypeDto.builder()
                  .id(person.documentType().id())
                  .name(person.documentType().name())
                  .build())
              .build();
        });
    when(this.personRestMapper.toDomain(any(PersonRequestDto.class)))
        .thenAnswer(invocation -> {
          PersonRequestDto personRequestDto = invocation.getArgument(0);
          return Person.builder()
              .firstName(personRequestDto.firstName())
              .lastName(personRequestDto.lastName())
              .documentType(DocumentType.builder()
                  .id(personRequestDto.documentType().id())
                  .name(personRequestDto.documentType().name())
                  .build())
              .build();
        });
  }

//  @Test
//  @DisplayName("testFindAll() -> Good case [not empty]")
//  void testFindAll() {
//    // Arrange
//    var documentTypes = List.of(DocumentType.builder()
//        .id(UUID.randomUUID())
//        .name("DNI")
//        .build(), DocumentType.builder()
//        .id(UUID.randomUUID())
//        .name("NIE")
//        .build());
//
//    when(this.findPersonUseCase.findAll()).thenReturn(Mono.just(documentTypes));
//
//    // Act
//    var response = this.webTestClient.get().uri("/api/v1/document-types")
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isOk()
//        .expectBodyList(DocumentTypeResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertFalse(response.isEmpty());
//    assertAll("Validate each element", () -> {
//      for (var i = 0; i < documentTypes.size(); i++) {
//        assertEquals(response.get(i).id(), documentTypes.get(i).id());
//        assertEquals(response.get(i).name(), documentTypes.get(i).name());
//      }
//    });
//
//    verify(this.findPersonUseCase, times(1)).findAll();
//    verify(this.personRestMapper, times(documentTypes.size()))
//        .toResponse(any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testFindAll() -> Good case [empty]")
//  void testFindAllEmpty() {
//    // Arrange
//    when(this.findPersonUseCase.findAll()).thenReturn(Mono.just(Collections.emptyList()));
//
//    // Act
//    var response = this.webTestClient.get().uri("/api/v1/document-types")
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isOk()
//        .expectBodyList(DocumentTypeResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertTrue(response.isEmpty());
//
//    verify(this.findPersonUseCase, times(1)).findAll();
//    verify(this.personRestMapper, times(0)).toResponse(any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testFindById() -> Good case")
//  void testFindById() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//    var documentTypeSaved = DocumentType.builder()
//        .id(documentTypeId)
//        .name("DNI")
//        .build();
//    var documentTypeResponseDto = DocumentTypeResponseDto.builder()
//        .id(documentTypeId)
//        .name(documentTypeSaved.name())
//        .build();
//
//    when(this.findPersonUseCase.findById(any(UUID.class)))
//        .thenReturn(Mono.just(documentTypeSaved));
//    when(this.personRestMapper.toResponse(any(DocumentType.class)))
//        .thenReturn(documentTypeResponseDto);
//
//    // Act
//    var response = webTestClient.get().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isOk()
//        .expectBody(DocumentTypeResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertThat(documentTypeSaved.id()).isEqualTo(response.id());
//    assertThat(documentTypeSaved.name()).isEqualTo(response.name());
//
//    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
//    verify(this.personRestMapper, times(1)).toResponse(any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testFindById() -> Bad case [not found]")
//  void testFindByIdObjectNotFoundException() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//
//    when(this.findPersonUseCase.findById(any(UUID.class)))
//        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
//            String.format("Document type with id '%s' was not found", documentTypeId))));
//
//    // Act
//    var response = this.webTestClient.get().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isNotFound()
//        .expectBody(ErrorResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");
//
//    verify(this.personRestMapper, times(0)).toResponse(any(DocumentType.class));
//    verify(this.findPersonUseCase, times(1)).findById(any(UUID.class));
//  }
//
//  @Test
//  @DisplayName("testCreate()")
//  void testCreate() {
//    // Arrange
//    var documentTypeIdCreated = UUID.randomUUID();
//    var documentType = DocumentType.builder()
//        .name("DNI")
//        .build();
//    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
//        .name("DNI")
//        .build();
//
//    when(this.personRestMapper.toDomain(any(DocumentTypeRequestDto.class)))
//        .thenReturn(documentType);
//    when(this.createPersonUseCase.create(any(DocumentType.class)))
//        .thenReturn(Mono.just(documentTypeIdCreated));
//
//    // Act
//    var response = webTestClient.post().uri("/api/v1/document-types")
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .bodyValue(documentTypeRequestDto)
//        .exchange()
//        .expectStatus().isCreated()
//        .returnResult(String.class)
//        .getResponseHeaders().getFirst(HttpHeaders.LOCATION);
//
//    // Assert
//    assertNotNull(response);
//    assertThat(response).isEqualTo("/api/v1/document-types/" + documentTypeIdCreated);
//
//    verify(this.personRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
//    verify(this.createPersonUseCase, times(1)).create(any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testUpdate() -> Good case")
//  void testUpdate() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//    var documentType = DocumentType.builder()
//        .name("DNI")
//        .build();
//    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
//        .name("DNI")
//        .build();
//
//    when(this.personRestMapper.toDomain(any(DocumentTypeRequestDto.class)))
//        .thenReturn(documentType);
//    when(this.updatePersonUseCase.update(any(UUID.class), any(DocumentType.class)))
//        .thenReturn(Mono.empty());
//
//    // Act
//    webTestClient.put().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .bodyValue(documentTypeRequestDto)
//        .exchange()
//        .expectStatus().isNoContent();
//
//    // Assert
//    verify(this.personRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
//    verify(this.updatePersonUseCase, times(1)).update(any(UUID.class),
//        any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testUpdate() -> Bad case [not found]")
//  void testUpdateObjectNotFoundException() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//    var documentTypeRequestDto = DocumentTypeRequestDto.builder()
//        .name("DNI")
//        .build();
//
//    when(this.updatePersonUseCase.update(any(UUID.class), any(DocumentType.class)))
//        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
//            String.format("Document type with id '%s' was not found", documentTypeId))));
//
//    // Act
//    var response = this.webTestClient.put().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .bodyValue(documentTypeRequestDto)
//        .exchange()
//        .expectStatus().isNotFound()
//        .expectBody(ErrorResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");
//
//    verify(this.personRestMapper, times(1)).toDomain(any(DocumentTypeRequestDto.class));
//    verify(this.updatePersonUseCase, times(1)).update(any(UUID.class),
//        any(DocumentType.class));
//  }
//
//  @Test
//  @DisplayName("testDelete() -> Good case")
//  void testDelete() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//
//    when(this.deletePersonUseCase.delete(any(UUID.class)))
//        .thenReturn(Mono.empty());
//
//    // Act
//    webTestClient.delete().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isNoContent();
//
//    // Assert
//    verify(this.deletePersonUseCase, times(1)).delete(any(UUID.class));
//  }
//
//  @Test
//  @DisplayName("testDelete() -> Bad case [not found]")
//  void testDeleteObjectNotFoundException() {
//    // Arrange
//    var documentTypeId = UUID.randomUUID();
//
//    when(this.deletePersonUseCase.delete(any(UUID.class)))
//        .thenReturn(Mono.error(() -> new ObjectNotFoundException("DOCUMENT_TYPE_NOT_FOUND",
//            String.format("Document type with id '%s' was not found", documentTypeId))));
//
//    // Act
//    var response = this.webTestClient.delete().uri("/api/v1/document-types/{id}", documentTypeId)
//        .header(HttpHeaders.ACCEPT, "application/json")
//        .exchange()
//        .expectStatus().isNotFound()
//        .expectBody(ErrorResponseDto.class)
//        .returnResult()
//        .getResponseBody();
//
//    // Assert
//    assertNotNull(response);
//    assertThat(response.key()).isEqualTo("DOCUMENT_TYPE_NOT_FOUND");
//
//    verify(this.deletePersonUseCase, times(1)).delete(any(UUID.class));
//  }

}
