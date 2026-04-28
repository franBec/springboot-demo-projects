package dev.pollito.spring_kotlin.sakila.staff.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.generated.entity.Staff
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StaffJpaRepository : JpaRepository<Staff, Int> {
  fun findByUsername(username: String): Optional<Staff>
}
