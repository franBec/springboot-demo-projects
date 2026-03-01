package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import dev.pollito.spring_java.config.mapper.MapperSpringConfig;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface FilmRestMapper
    extends Converter<Film, dev.pollito.spring_java.generated.model.Film> {
  @Override
  dev.pollito.spring_java.generated.model.Film convert(Film source);
}
