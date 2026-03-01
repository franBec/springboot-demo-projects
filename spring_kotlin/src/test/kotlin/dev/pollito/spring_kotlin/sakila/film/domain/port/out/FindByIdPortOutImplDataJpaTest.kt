package dev.pollito.spring_kotlin.sakila.film.domain.port.out

import dev.pollito.spring_kotlin.sakila.film.adapter.out.jpa.FilmJpaMapperImpl
import kotlin.test.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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
class FindByIdPortOutImplDataJpaTest {

  @Autowired private lateinit var findByIdPortOut: FindByIdPortOut

  @Test
  fun `findById finds an Entity and returns a Domain model`() {
    val id = 1
    val result = findByIdPortOut.findById(id)

    assertNotNull(result)
    assertEquals(id, result.id)
  }
}
