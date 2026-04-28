package dev.pollito.spring_groovy.sakila.staff.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
@CompileStatic
interface StaffJpaRepository extends JpaRepository<dev.pollito.spring_groovy.sakila.generated.entity.Staff, Integer> {
  Optional<dev.pollito.spring_groovy.sakila.generated.entity.Staff> findByUsername(String username)
}
