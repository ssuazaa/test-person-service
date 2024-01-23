package com.test.personservice.documenttype.infrastructure.config.bean;

import com.test.personservice.documenttype.application.usecase.CreateDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.application.usecase.DeleteDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.application.usecase.FindDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.application.usecase.UpdateDocumentTypeUseCaseImpl;
import com.test.personservice.documenttype.domain.port.in.CreateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.DeleteDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.FindDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.in.UpdateDocumentTypeUseCase;
import com.test.personservice.documenttype.domain.port.out.DocumentTypeRepositoryOut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentTypeBeanConfig {

  private final DocumentTypeRepositoryOut documentTypeRepositoryOut;

  public DocumentTypeBeanConfig(DocumentTypeRepositoryOut documentTypeRepositoryOut) {
    this.documentTypeRepositoryOut = documentTypeRepositoryOut;
  }

  @Bean
  public FindDocumentTypeUseCase findDocumentTypeUseCase() {
    return new FindDocumentTypeUseCaseImpl(this.documentTypeRepositoryOut);
  }

  @Bean
  public CreateDocumentTypeUseCase createDocumentTypeUseCase() {
    return new CreateDocumentTypeUseCaseImpl(this.documentTypeRepositoryOut);
  }

  @Bean
  public UpdateDocumentTypeUseCase updateDocumentTypeUseCase() {
    return new UpdateDocumentTypeUseCaseImpl(findDocumentTypeUseCase(),
        this.documentTypeRepositoryOut);
  }

  @Bean
  public DeleteDocumentTypeUseCase deleteDocumentTypeUseCase() {
    return new DeleteDocumentTypeUseCaseImpl(findDocumentTypeUseCase(),
        this.documentTypeRepositoryOut);
  }

}
