package dev.pollito.spring_groovy.sakila.film.adapter.out.jpa

import dev.pollito.spring_groovy.generated.entity.Film as EntityFilm
import dev.pollito.spring_groovy.sakila.film.domain.model.Film as DomainFilm
import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.modelmapper.TypeMap
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component

@Component
@CompileStatic
class FilmJpaMapper {
    private final ModelMapper mapper
    private TypeMap<EntityFilm, DomainFilm> typeMap

    FilmJpaMapper(ModelMapper mapper) {
        this.mapper = mapper
        configureTypeMap()
    }

    private void configureTypeMap() {
        typeMap = mapper.createTypeMap(EntityFilm, DomainFilm)
        typeMap.addMappings { mapping ->
            mapping.skip(DomainFilm::setReleaseYear)
            mapping.map({ src -> src.filmId }, DomainFilm::setId)
            mapping.map({ src -> src.languageByLanguageId.name }, DomainFilm::setLanguage)
        }
        typeMap.setPostConverter { ctx ->
            DomainFilm destination = ctx.destination
            destination.releaseYear = ctx.source.releaseYear?.year
            destination
        }
    }

    DomainFilm convert(EntityFilm source) {
        mapper.map(source, DomainFilm)
    }

    Page<DomainFilm> convert(Page<EntityFilm> source) {
        source.map { convert(it as EntityFilm) }
    }
}