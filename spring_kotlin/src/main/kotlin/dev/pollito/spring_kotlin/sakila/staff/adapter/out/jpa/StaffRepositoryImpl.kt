package dev.pollito.spring_kotlin.sakila.staff.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff
import dev.pollito.spring_kotlin.sakila.staff.domain.port.out.StaffRepository
import java.util.Optional
import org.springframework.stereotype.Service

@Service
class StaffRepositoryImpl(
    private val staffJpaRepository: StaffJpaRepository,
    private val staffJpaMapper: StaffJpaMapper,
) : StaffRepository {

  override fun findByUsername(username: String): Optional<Staff> {
    return staffJpaRepository.findByUsername(username).map(staffJpaMapper::map)
  }
}
