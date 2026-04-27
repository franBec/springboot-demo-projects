package dev.pollito.spring_java.sakila.staff.adapter.out.jpa;

import dev.pollito.spring_java.sakila.generated.entity.Staff;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffJpaRepository extends JpaRepository<Staff, Integer> {
  Optional<Staff> findByUsername(String username);
}
