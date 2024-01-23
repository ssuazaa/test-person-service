package com.test.personservice.person.infrastructure.adapter.in.rest.adapter;

import com.test.personservice.person.domain.model.Person;
import com.test.personservice.person.domain.port.in.CreatePersonUseCase;
import com.test.personservice.person.domain.port.in.DeletePersonUseCase;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonRequestDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.dto.PersonResponseDto;
import com.test.personservice.person.infrastructure.adapter.in.rest.mapper.PersonRestMapper;
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
@RequestMapping("/api/v1/persons")
public class PersonRestController {

  private final FindPersonUseCase findPersonUseCase;
  private final CreatePersonUseCase createPersonUseCase;
  private final UpdatePersonUseCase updatePersonUseCase;
  private final DeletePersonUseCase deletePersonUseCase;
  private final PersonRestMapper mapper;

  public PersonRestController(FindPersonUseCase findPersonUseCase,
      CreatePersonUseCase createPersonUseCase,
      UpdatePersonUseCase updatePersonUseCase,
      DeletePersonUseCase deletePersonUseCase,
      PersonRestMapper mapper) {
    this.findPersonUseCase = findPersonUseCase;
    this.createPersonUseCase = createPersonUseCase;
    this.updatePersonUseCase = updatePersonUseCase;
    this.deletePersonUseCase = deletePersonUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  public Mono<ResponseEntity<List<PersonResponseDto>>> findAll() {
    return this.findPersonUseCase.findAll()
        .map((List<Person> persons) -> persons.stream()
            .map(this.mapper::toResponse)
            .toList())
        .map(ResponseEntity::ok);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<PersonResponseDto>> findById(@PathVariable UUID id) {
    return this.findPersonUseCase.findById(id)
        .map(this.mapper::toResponse)
        .map(ResponseEntity::ok);
  }

  @PostMapping
  public Mono<ResponseEntity<Void>> create(
      @RequestBody @Valid PersonRequestDto personRequestDto,
      UriComponentsBuilder uriBuilder) {
    return this.createPersonUseCase.create(this.mapper.toDomain(personRequestDto))
        .map((UUID id) -> ResponseEntity.created(uriBuilder
                .path("/api/v1/persons/{id}")
                .buildAndExpand(id)
                .toUri())
            .build());
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<Void>> update(@PathVariable UUID id,
      @RequestBody @Valid PersonRequestDto personRequestDto) {
    return this.updatePersonUseCase.update(id, this.mapper.toDomain(personRequestDto))
        .then(Mono.just(ResponseEntity.noContent().build()));
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
    return this.deletePersonUseCase.delete(id)
        .then(Mono.just(ResponseEntity.noContent().build()));
  }

}
