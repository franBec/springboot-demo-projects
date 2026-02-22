package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.junit5.MockKExtension
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FindByIdPortInImplTest {
  @InjectMockKs private lateinit var findByIdPortInImpl: FindByIdPortInImpl

  @Test
  fun `findById returns a domain model`() {
    assertNotNull(findByIdPortInImpl.findById(1))
  }
}
