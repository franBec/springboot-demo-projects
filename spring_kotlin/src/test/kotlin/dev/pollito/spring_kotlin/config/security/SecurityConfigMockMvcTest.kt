package dev.pollito.spring_kotlin.config.security

import kotlin.test.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = ["/sakila-schema.sql", "/sakila-data.sql"])
class SecurityConfigMockMvcTest {

  @Autowired private lateinit var mockMvc: MockMvc

  @Test
  fun `GET films returns 200`() {
    mockMvc
        .get("/api/films") { accept = MediaType.APPLICATION_JSON }
        .andExpect { status { isOk() } }
  }

  @Test
  fun `GET films by id returns 200`() {
    mockMvc
        .get("/api/films/1") { accept = MediaType.APPLICATION_JSON }
        .andExpect { status { isOk() } }
  }

  @Test
  fun `POST films without auth returns 401`() {
    mockMvc
        .post("/api/films") {
          contentType = MediaType.APPLICATION_JSON
          content =
              "{\"title\":\"TEST\",\"language\":\"English\",\"rentalDuration\":3,\"rentalRate\":4.99,\"replacementCost\":20.99}"
        }
        .andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `POST login with valid credentials returns 200`() {
    mockMvc
        .post("/api/auth/login") {
          contentType = MediaType.APPLICATION_JSON
          content = "{\"username\":\"Mike\",\"password\":\"password\"}"
        }
        .andExpect { status { isOk() } }
  }

  @Test
  fun `GET auth me without auth returns 401`() {
    mockMvc
        .get("/api/auth/me") { accept = MediaType.APPLICATION_JSON }
        .andExpect { status { isUnauthorized() } }
  }

  @Test
  fun `GET actuator health returns 200`() {
    mockMvc.get("/actuator/health").andExpect { status { isOk() } }
  }

  @Test
  fun `GET h2-console returns 404 in test profile`() {
    mockMvc.get("/h2-console").andExpect { status { isNotFound() } }
  }

  @Test
  fun `GET error returns 500`() {
    mockMvc.get("/error").andExpect { status { isInternalServerError() } }
  }
}
