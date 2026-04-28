package dev.pollito.spring_kotlin.sakila.auth.adapter.`in`.rest

import dev.pollito.spring_kotlin.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_kotlin.sakila.generated.model.UserDetails
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.springframework.security.core.GrantedAuthority

@Mapper(componentModel = "spring")
interface AuthRestMapper {

  @Mapping(source = "username", target = "username")
  @Mapping(source = "accountNonLocked", target = "accountNonLocked")
  @Mapping(source = "authorities", target = "authorities", qualifiedByName = ["mapAuthorities"])
  @Mapping(source = "staff", target = "staff")
  fun map(sakilaUserDetails: SakilaUserDetails): UserDetails

  @Named("mapAuthorities")
  fun mapAuthorities(authorities: Collection<GrantedAuthority>): List<String> {
    return authorities.mapNotNull { it.authority }
  }
}
