package dev.pollito.spring_java.config.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.extensions.spring.SpringMapperConfig;

@MapperConfig(componentModel = "spring")
@SpringMapperConfig(conversionServiceAdapterPackage = "dev.pollito.spring_java.config.mapper")
public interface MapperSpringConfig {}
