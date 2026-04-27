package dev.pollito.spring_java.sakila.staff.adapter.out.jpa;

import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StaffJpaMapper {

  @Mapping(source = "staffId", target = "id")
  Staff map(dev.pollito.spring_java.sakila.generated.entity.Staff entity);
}
