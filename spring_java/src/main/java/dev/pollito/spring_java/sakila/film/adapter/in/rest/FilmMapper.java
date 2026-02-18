package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import dev.pollito.spring_java.config.mapper.MapperSpringConfig;
import dev.pollito.spring_java.sakila.film.adapter.in.rest.dto.FilmResponse;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.jspecify.annotations.Nullable;
import org.mapstruct.Mapper;
import org.springframework.core.convert.converter.Converter;

@Mapper(config = MapperSpringConfig.class)
public interface FilmMapper extends Converter<Film, FilmResponse> {
  @Override
  FilmResponse convert(@Nullable Film source);
}
