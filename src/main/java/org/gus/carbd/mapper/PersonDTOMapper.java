package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDTO;
import org.gus.carbd.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonDTOMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "passport", source = "passport")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "surname", source = "surname")
    @Mapping(target = "patronymic", source = "patronymic")
    @Mapping(target = "vehicles", source = "vehicles")
    PersonDTO toPersonDTO(Person person);
}
