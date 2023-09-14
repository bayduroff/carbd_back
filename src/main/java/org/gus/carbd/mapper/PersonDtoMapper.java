package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PersonDtoMapper {

    PersonDto toPersonDto(Person person);

    Person toPerson(PersonDto personDTO);

    List<PersonDto> toPersonDtoList(List<Person> people);

    Set<PersonDto> toPersonDtoSet(Set<Person> people);

    //не обращаться к trans методу изнутри PersonDtoMapper
    void updatePerson(@MappingTarget Person person, PersonDto changedPersonDto);
}
