package com.test.personservice.shared.infrastructure.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class CorsConfig implements WebFluxConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("*")
        .maxAge(3600);
  }

}
