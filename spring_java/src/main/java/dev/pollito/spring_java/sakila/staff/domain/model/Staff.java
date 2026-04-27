package dev.pollito.spring_java.sakila.staff.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Staff {
  private Integer id;
  private String firstName;
  private String lastName;
  private String username;
  private String password;
  private String email;
  private boolean active;
}
