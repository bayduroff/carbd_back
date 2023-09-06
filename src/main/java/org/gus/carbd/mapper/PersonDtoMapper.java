package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDTO;
import org.gus.carbd.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonDtoMapper {

    PersonDTO toPersonDto(Person person);

    List<PersonDTO> toPersonDtoList(List<Person> people);

    Person toPerson(PersonDTO personDTO);

    //не обращаться к trans методу изнутри PersonDtoMapper
    void updatePerson(@MappingTarget Person person, PersonDTO changedPersonDTO);
}
