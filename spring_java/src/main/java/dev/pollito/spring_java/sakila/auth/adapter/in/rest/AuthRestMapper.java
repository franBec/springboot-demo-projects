package dev.pollito.spring_java.sakila.auth.adapter.in.rest;

import dev.pollito.spring_java.config.security.userdetails.SakilaUserDetails;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface AuthRestMapper {

  @Mapping(
      target = "authorities",
      expression = "java(mapAuthorities(sakilaUserDetails.getAuthorities()))")
  dev.pollito.spring_java.sakila.generated.model.UserDetails map(
      SakilaUserDetails sakilaUserDetails);

  default List<String> mapAuthorities(
      java.util.Collection<? extends GrantedAuthority> authorities) {
    if (authorities == null) {
      return null;
    }
    return authorities.stream().map(GrantedAuthority::getAuthority).toList();
  }
}
