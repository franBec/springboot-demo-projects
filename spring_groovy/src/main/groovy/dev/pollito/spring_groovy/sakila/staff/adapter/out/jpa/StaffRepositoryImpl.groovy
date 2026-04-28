package dev.pollito.spring_groovy.sakila.staff.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import dev.pollito.spring_groovy.sakila.staff.domain.port.out.StaffRepository
import groovy.transform.CompileStatic
import org.springframework.stereotype.Service

@Service
@CompileStatic
class StaffRepositoryImpl implements StaffRepository {
  private final StaffJpaRepository repository
  private final StaffJpaMapper mapper

  StaffRepositoryImpl(StaffJpaRepository repository, StaffJpaMapper mapper) {
    this.repository = repository
    this.mapper = mapper
  }

  @Override
  Optional<Staff> findByUsername(String username) {
    repository.findByUsername(username).map { mapper.map(it) }
  }
}
