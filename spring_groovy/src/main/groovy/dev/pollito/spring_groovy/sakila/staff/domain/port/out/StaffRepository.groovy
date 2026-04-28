package dev.pollito.spring_groovy.sakila.staff.domain.port.out

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import groovy.transform.CompileStatic

@CompileStatic
interface StaffRepository {
  Optional<Staff> findByUsername(String username)
}
