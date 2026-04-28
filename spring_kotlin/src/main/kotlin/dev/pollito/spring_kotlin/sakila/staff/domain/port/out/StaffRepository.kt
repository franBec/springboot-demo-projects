package dev.pollito.spring_kotlin.sakila.staff.domain.port.out

import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import java.util.Optional

interface StaffRepository {
  fun findByUsername(username: String): Optional<Staff>
}
