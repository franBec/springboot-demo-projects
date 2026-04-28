package dev.pollito.spring_groovy.sakila.staff.adapter.out.jpa

import dev.pollito.spring_groovy.sakila.staff.domain.model.Staff
import groovy.transform.CompileStatic
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
@CompileStatic
class StaffJpaMapper {
  private final ModelMapper mapper

  StaffJpaMapper(ModelMapper mapper) {
    this.mapper = mapper
  }

  Staff map(dev.pollito.spring_groovy.sakila.generated.entity.Staff entity) {
    def staff = new Staff()
    staff.id = entity.staffId
    staff.firstName = entity.firstName
    staff.lastName = entity.lastName
    staff.username = entity.username
    staff.password = entity.password
    staff.email = entity.email
    staff.active = entity.active
    staff
  }
}
