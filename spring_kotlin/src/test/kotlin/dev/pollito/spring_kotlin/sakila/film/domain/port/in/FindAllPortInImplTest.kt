package dev.pollito.spring_kotlin.sakila.film.domain.port.`in`

import dev.pollito.spring_kotlin.sakila.film.domain.port.out.FindAllPortOut
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

@ExtendWith(MockKExtension::class)
class FindAllPortInImplTest {
  @MockK private lateinit var findAllPortOut: FindAllPortOut
  @InjectMockKs private lateinit var findAllPortInImpl: FindAllPortInImpl

  @Test
  fun `findAll returns a page`() {
    every { findAllPortOut.findAll(any<Pageable>()) } returns
        PageImpl(listOf(), PageRequest.of(0, 20), 0)
    assertNotNull(findAllPortInImpl.findAll(PageRequest.of(0, 20)))
  }
}
