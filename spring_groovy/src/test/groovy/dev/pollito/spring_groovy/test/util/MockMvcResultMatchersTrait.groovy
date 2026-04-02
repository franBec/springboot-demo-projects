package dev.pollito.spring_groovy.test.util

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.ResultMatcher

trait MockMvcResultMatchersTrait {

  ResultMatcher hasStandardApiResponseFields(String expectedInstance, HttpStatus expectedStatus) {
    { result ->
      jsonPath('$.instance').value(expectedInstance).match(result)
      jsonPath('$.status').value(expectedStatus.value()).match(result)
      jsonPath('$.timestamp').exists().match(result)
      jsonPath('$.trace').exists().match(result)
    } as ResultMatcher
  }

  ResultMatcher hasErrorFields(HttpStatus expectedStatus) {
    { result ->
      jsonPath('$.title').value(expectedStatus.reasonPhrase).match(result)
    } as ResultMatcher
  }

  ResultMatcher hasPageFields() {
    { result ->
      jsonPath('$.data.content').isArray().match(result)
      jsonPath('$.data.pageable.pageNumber').isNumber().match(result)
      jsonPath('$.data.pageable.pageSize').isNumber().match(result)
      jsonPath('$.data.totalElements').isNumber().match(result)
      jsonPath('$.data.totalPages').isNumber().match(result)
    } as ResultMatcher
  }
}