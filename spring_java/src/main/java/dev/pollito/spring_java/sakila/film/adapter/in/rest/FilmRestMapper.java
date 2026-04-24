package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.generated.model.FilmListResponseAllOfData;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = SPRING)
public interface FilmRestMapper {
  Film map(dev.pollito.spring_java.sakila.generated.model.FilmFields source);

  dev.pollito.spring_java.sakila.generated.model.Film map(Film source);

  FilmListResponseAllOfData map(Page<Film> source);
}
