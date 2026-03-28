package dev.pollito.spring_groovy.config.mapper

import dev.pollito.spring_groovy.sakila.film.domain.model.FilmLanguage as DomainFilmLanguage
import dev.pollito.spring_groovy.sakila.film.domain.model.FilmRating as DomainFilmRating
import dev.pollito.spring_groovy.sakila.generated.model.FilmLanguage as GeneratedFilmLanguage
import dev.pollito.spring_groovy.sakila.generated.model.FilmRating as GeneratedFilmRating
import groovy.transform.CompileStatic
import org.modelmapper.AbstractConverter
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@CompileStatic
class ModelMapperConfig {
  @Bean
  ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper()
    mapper.addConverter(new AbstractConverter<DomainFilmLanguage, GeneratedFilmLanguage>() {
          @Override
          protected GeneratedFilmLanguage convert(DomainFilmLanguage source) {
            source == null ? null : GeneratedFilmLanguage.fromValue(source.getValue())
          }
        })
    mapper.addConverter(new AbstractConverter<DomainFilmRating, GeneratedFilmRating>() {
          @Override
          protected GeneratedFilmRating convert(DomainFilmRating source) {
            source == null ? null : GeneratedFilmRating.fromValue(source.getValue())
          }
        })
    mapper
  }
}
