package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.pollito.spring_java.common.util.EnumUtils;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(
    componentModel = SPRING,
    imports = {
      EnumUtils.class,
      FilmLanguage.class,
      FilmRating.class,
      OffsetDateTime.class,
      ZoneOffset.class
    })
public interface FilmJpaMapper {

  @Mapping(target = "id", source = "filmId")
  @Mapping(
      target = "language",
      expression =
          "java(source.getLanguageByLanguageId() != null && source.getLanguageByLanguageId().getName() != null ? EnumUtils.fromValue(FilmLanguage.class, source.getLanguageByLanguageId().getName()) : null)")
  @Mapping(
      target = "originalLanguage",
      expression =
          "java(source.getLanguageByOriginalLanguageId() != null && source.getLanguageByOriginalLanguageId().getName() != null ? EnumUtils.fromValue(FilmLanguage.class, source.getLanguageByOriginalLanguageId().getName()) : null)")
  @Mapping(
      target = "releaseYear",
      expression =
          "java(source.getReleaseYear() != null ? source.getReleaseYear().getYear() : null)")
  @Mapping(
      target = "rating",
      expression =
          "java(source.getRating() != null ? EnumUtils.fromValue(FilmRating.class, source.getRating()) : null)")
  @Mapping(
      target = "lastUpdate",
      expression =
          "java(source.getLastUpdate() != null ? source.getLastUpdate().atOffset(ZoneOffset.UTC) : null)")
  Film map(dev.pollito.spring_java.sakila.generated.entity.Film source);

  default Page<Film> map(
      @NonNull Page<dev.pollito.spring_java.sakila.generated.entity.Film> source) {
    return source.map(this::map);
  }
}
