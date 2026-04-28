package dev.pollito.spring_groovy.sakila.auth.adapter.in.rest

import dev.pollito.spring_groovy.config.security.userdetails.SakilaUserDetails
import dev.pollito.spring_groovy.sakila.generated.model.Staff
import dev.pollito.spring_groovy.sakila.generated.model.UserDetails
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Component

@Component
class AuthRestMapper {
  private final ModelMapper mapper
  private static final List<String> TRIVIAL_FIELDS = [
    'username',
    'password',
    'accountNonLocked'
  ]

  AuthRestMapper(ModelMapper mapper) {
    this.mapper = mapper
    configureTypeMaps()
  }

  private void configureTypeMaps() {
    mapper.createTypeMap(SakilaUserDetails, UserDetails).setConverter { ctx ->
      def s = ctx.source
      def d = new UserDetails()
      TRIVIAL_FIELDS.each { d[it] = s[it] }
      d.authorities = s.authorities?.collect { it.authority }
      d.staff = mapper.map(s.staff, Staff)
      d
    }
  }

  UserDetails map(SakilaUserDetails source) {
    if (source == null) {
      return null
    }
    mapper.map(source, UserDetails)
  }
}
