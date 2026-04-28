package dev.pollito.spring_groovy.sakila.staff.domain.model

import groovy.transform.CompileStatic

@CompileStatic
class Staff {
  Integer id
  String firstName
  String lastName
  String username
  String password
  String email
  boolean active
}
