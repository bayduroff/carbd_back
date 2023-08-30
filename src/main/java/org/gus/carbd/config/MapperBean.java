package org.gus.carbd.config;

import org.gus.carbd.mapper.PersonDTOMapper;
import org.gus.carbd.mapper.VehicleDTOMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperBean {
    @Bean
    public PersonDTOMapper personDTOMapper() {
        return Mappers.getMapper(PersonDTOMapper.class);
    }

    @Bean
    public VehicleDTOMapper vehicleDTOMapper() {
        return Mappers.getMapper(VehicleDTOMapper.class);
    }
}
