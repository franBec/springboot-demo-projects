package dev.pollito.spring_java.sakila.film.adapter.out.jpa;

import static java.util.Objects.requireNonNull;

import dev.pollito.spring_java.config.mapper.MapperSpringConfig;
import dev.pollito.spring_java.sakila.film.domain.model.Film;
import java.time.LocalDate;
import org.jspecify.annotations.NonNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;

@Mapper(config = MapperSpringConfig.class)
public interface FilmJpaMapper
    extends Converter<dev.pollito.spring_java.generated.entity.Film, Film> {

  @Override
  @Mapping(target = "id", source = "filmId")
  @Mapping(target = "language", source = "languageByLanguageId.name")
  Film convert(dev.pollito.spring_java.generated.entity.Film source);

  default Integer mapReleaseYear(LocalDate releaseYear) {
    return releaseYear != null ? releaseYear.getYear() : null;
  }

  default Page<Film> convert(@NonNull Page<dev.pollito.spring_java.generated.entity.Film> source) {
    return source.map(it -> requireNonNull(this.convert(it)));
  }
}
