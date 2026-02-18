package dev.pollito.spring_groovy.config.mapper

import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@CompileStatic
class ModelMapperConfig {
  @Bean
  ModelMapper modelMapper() {
    new ModelMapper()
  }
}
