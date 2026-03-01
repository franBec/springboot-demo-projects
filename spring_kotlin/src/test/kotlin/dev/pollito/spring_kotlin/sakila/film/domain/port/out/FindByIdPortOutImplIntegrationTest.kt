package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaMapperImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS

@DataJpaTest
@ActiveProfiles("test")
@Import(FindByIdPortOutImpl::class, FilmJpaMapperImpl::class)
@Sql(
    scripts = ["/sakila-schema.sql", "/sakila-data.sql"],
    executionPhase = BEFORE_TEST_CLASS,
)
class FindByIdPortOutImplIntegrationTest {

  @Autowired private lateinit var findByIdPortOut: FindByIdPortOut

  @ParameterizedTest
  @CsvSource("1, 2006", "10, ")
  fun `findById when film exists should return film`(filmId: Int, expectedYear: Int?) {
    val result = findByIdPortOut.findById(filmId)

    Assertions.assertNotNull(result)
    Assertions.assertEquals(filmId, result.id)
    Assertions.assertEquals(expectedYear, result.releaseYear)
  }
}
