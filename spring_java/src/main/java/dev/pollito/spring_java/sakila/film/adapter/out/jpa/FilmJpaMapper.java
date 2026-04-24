package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import dev.pollito.spring_java.config.enums.EnumUtils;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import dev.pollito.spring_java.sakila.generated.entity.Language;
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
      ZoneOffset.class,
      Language.class
    })
public interface FilmJpaMapper {

  @Mapping(target = "id", source = "filmId")
  @Mapping(
      target = "language",
      expression =
          "java(EnumUtils.fromValue(FilmLanguage.class, source.getLanguageByLanguageId().getName()))")
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
      expression = "java(source.getLastUpdate().atOffset(ZoneOffset.UTC))")
  Film map(dev.pollito.spring_java.sakila.generated.entity.Film source);

  @Mapping(target = "filmId", ignore = true)
  @Mapping(target = "lastUpdate", ignore = true)
  @Mapping(target = "languageByLanguageId", source = "language")
  @Mapping(target = "languageByOriginalLanguageId", source = "originalLanguage")
  @Mapping(target = "inventories", ignore = true)
  @Mapping(target = "filmCategories", ignore = true)
  @Mapping(target = "filmActors", ignore = true)
  @Mapping(
      target = "releaseYear",
      expression =
          "java(source.getReleaseYear() != null ? java.time.LocalDate.of(source.getReleaseYear(), 1, 1) : null)")
  @Mapping(
      target = "rating",
      expression = "java(source.getRating() != null ? source.getRating().getValue() : null)")
  dev.pollito.spring_java.sakila.generated.entity.Film map(
      Film source, Language language, Language originalLanguage);

  default Page<Film> map(
      @NonNull Page<dev.pollito.spring_java.sakila.generated.entity.Film> source) {
    return source.map(this::map);
  }
}
