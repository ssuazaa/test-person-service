package com.test.personservice.documenttype.infrastructure.adapter.in.rest.adapter;

import com.test.personservice.documenttype.domain.model.DocumentType;
import com.test.personservice.documenttype.domain.port.in.CreateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.DeleteDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.UpdateDocumentTypeUseCase;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeRequestDto;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.dto.DocumentTypeResponseDto;
import com.test.personservice.documenttype.infrastructure.adapter.in.rest.mapper.DocumentTypeRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/api/v1/document-types")
public class DocumentTypeRestController {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final CreateDocumentTypeUseCase createDocumentTypeUseCase;
  private final UpdateDocumentTypeUseCase updateDocumentTypeUseCase;
  private final DeleteDocumentTypeUseCase deleteDocumentTypeUseCase;
  private final DocumentTypeRestMapper mapper;

  public DocumentTypeRestController(FindDocumentTypeUseCase findDocumentTypeUseCase,
      CreateDocumentTypeUseCase createDocumentTypeUseCase,
      UpdateDocumentTypeUseCase updateDocumentTypeUseCase,
      DeleteDocumentTypeUseCase deleteDocumentTypeUseCase,
      DocumentTypeRestMapper mapper) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.createDocumentTypeUseCase = createDocumentTypeUseCase;
    this.updateDocumentTypeUseCase = updateDocumentTypeUseCase;
    this.deleteDocumentTypeUseCase = deleteDocumentTypeUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  public Mono<ResponseEntity<List<DocumentTypeResponseDto>>> findAll() {
    return this.findDocumentTypeUseCase.findAll()
        .map((List<DocumentType> documentTypes) -> documentTypes.stream()
            .map(this.mapper::toResponse)
            .toList())
        .map(ResponseEntity::ok);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<DocumentTypeResponseDto>> findById(@PathVariable UUID id) {
    return this.findDocumentTypeUseCase.findById(id)
        .map(this.mapper::toResponse)
        .map(ResponseEntity::ok);
  }

  @PostMapping
  public Mono<ResponseEntity<Void>> create(
      @RequestBody @Valid DocumentTypeRequestDto personRequestDto,
      UriComponentsBuilder uriBuilder) {
    return this.createDocumentTypeUseCase.create(this.mapper.toDomain(personRequestDto))
        .map((UUID id) -> ResponseEntity.created(uriBuilder
                .path("/api/v1/document-types/{id}")
                .buildAndExpand(id)
                .toUri())
            .build());
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Void>> update(@PathVariable UUID id,
      @RequestBody @Valid DocumentTypeRequestDto personRequestDto) {
    return this.updateDocumentTypeUseCase.update(id, this.mapper.toDomain(personRequestDto))
        .then(Mono.just(ResponseEntity.noContent().build()));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
    return this.deleteDocumentTypeUseCase.delete(id)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }

}
