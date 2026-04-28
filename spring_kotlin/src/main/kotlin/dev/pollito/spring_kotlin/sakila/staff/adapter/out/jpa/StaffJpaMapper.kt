package dev.pollito.spring_kotlin.sakila.staff.adapter.out.jpa

import dev.pollito.spring_kotlin.sakila.generated.entity.Staff
import dev.pollito.spring_kotlin.sakila.staff.domain.model.Staff as StaffDomain
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface StaffJpaMapper {

  @Mapping(source = "staffId", target = "id") fun map(staff: Staff): StaffDomain
}
