package dev.pollito.spring_groovy.test.util

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

import groovy.transform.CompileStatic
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultMatcher

@CompileStatic
trait MockMvcResultMatchersTrait {

  ResultMatcher hasStandardApiResponseFields(String expectedInstance, HttpStatus expectedStatus) {
    { result ->
      jsonPath('$.instance').value(expectedInstance).match(result as MvcResult)
      jsonPath('$.status').value(expectedStatus.value()).match(result as MvcResult)
      jsonPath('$.timestamp').exists().match(result as MvcResult)
      jsonPath('$.trace').exists().match(result as MvcResult)
    }
  }

  ResultMatcher hasErrorFields(HttpStatus expectedStatus) {
    { result ->
      jsonPath('$.title').value(expectedStatus.reasonPhrase).match(result as MvcResult)
    }
  }
}
