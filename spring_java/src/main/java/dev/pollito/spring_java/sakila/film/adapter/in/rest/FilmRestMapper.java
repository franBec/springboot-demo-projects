package dev.pollito.spring_java.sakila.film.adapter.in.rest;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.pollito.spring_java.sakila.film.domain.model.Film;
import org.mapstruct.Mapper;

@Mapper(componentModel = SPRING)
public interface FilmRestMapper {
  dev.pollito.spring_java.sakila.generated.model.Film map(Film source);
}
