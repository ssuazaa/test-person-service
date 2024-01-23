package com.test.personservice.person.infrastructure.config.bean;

import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.person.application.usecase.CreatePersonUseCaseImpl;
import com.test.personservice.person.application.usecase.DeletePersonUseCaseImpl;
import com.test.personservice.person.application.usecase.FindPersonUseCaseImpl;
import com.test.personservice.person.application.usecase.UpdatePersonUseCaseImpl;
import com.test.personservice.person.domain.port.in.CreatePersonUseCase;
import com.test.personservice.person.domain.port.in.DeletePersonUseCase;
import com.test.personservice.person.domain.port.in.FindPersonUseCase;
import com.test.personservice.person.domain.port.in.UpdatePersonUseCase;
import com.test.personservice.person.domain.port.out.PersonRepositoryOut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersonBeanConfig {

  private final FindDocumentTypeUseCase findDocumentTypeUseCase;
  private final PersonRepositoryOut personRepositoryOut;

  public PersonBeanConfig(FindDocumentTypeUseCase findDocumentTypeUseCase,
      PersonRepositoryOut personRepositoryOut) {
    this.findDocumentTypeUseCase = findDocumentTypeUseCase;
    this.personRepositoryOut = personRepositoryOut;
  }

  @Bean
  public FindPersonUseCase findPersonUseCase() {
    return new FindPersonUseCaseImpl(this.findDocumentTypeUseCase, this.personRepositoryOut);
  }

  @Bean
  public CreatePersonUseCase createPersonUseCase() {
    return new CreatePersonUseCaseImpl(this.findDocumentTypeUseCase, this.personRepositoryOut);
  }

  @Bean
  public UpdatePersonUseCase updatePersonUseCase() {
    return new UpdatePersonUseCaseImpl(this.findDocumentTypeUseCase, findPersonUseCase(),
        this.personRepositoryOut);
  }

  @Bean
  public DeletePersonUseCase deletePersonUseCase() {
    return new DeletePersonUseCaseImpl(findPersonUseCase(), this.personRepositoryOut);
  }

}
