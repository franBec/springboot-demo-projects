package dev.pollito.spring_java.sakila.staff.domain.port.out;

import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import java.util.Optional;

public interface StaffRepository {
  Optional<Staff> findByUsername(String username);
}
