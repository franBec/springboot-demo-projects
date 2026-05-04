package dev.pollito.spring_java.config.web;

import dev.pollito.spring_java.sakila.film.domain.model.FilmLanguage;
import dev.pollito.spring_java.sakila.film.domain.model.FilmRating;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(
        new Converter<String, FilmLanguage>() {
          @Override
          public FilmLanguage convert(String source) {
            if (source == null || source.isBlank()) {
              return null;
            }
            return FilmLanguage.valueOf(source);
          }
        });
    registry.addConverter(
        new Converter<String, FilmRating>() {
          @Override
          public FilmRating convert(String source) {
            if (source == null || source.isBlank()) {
              return null;
            }
            return FilmRating.valueOf(source);
          }
        });
  }
}
