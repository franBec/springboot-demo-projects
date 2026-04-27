package dev.pollito.spring_java.sakila.staff.adapter.out.jpa;

import dev.pollito.spring_java.sakila.staff.domain.model.Staff;
import dev.pollito.spring_java.sakila.staff.domain.port.out.StaffRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffRepositoryImpl implements StaffRepository {

  private final StaffJpaRepository repository;
  private final StaffJpaMapper mapper;

  @Override
  public Optional<Staff> findByUsername(String username) {
    return repository.findByUsername(username).map(mapper::map);
  }
}
