package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.Person;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = PassportDtoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PersonDtoMapper {

    @Mapping(target = "passportDto", source = "passport")
    PersonDto toPersonDto(Person person);

    List<PersonDto> toPersonDtoList(List<Person> people);

    Set<PersonDto> toPersonDtoSet(Set<Person> people);

    @Mapping(target = "passport", source = "passportDto")
    Person toPerson(PersonDto personDTO);

    @AfterMapping
    default void linkPersonWithPassport(@MappingTarget Person person) {
        person.getPassport().setPerson(person);
    }

    @Mapping(target = "passport", source = "passportDto")
    void updatePerson(@MappingTarget Person person, PersonDto changedPersonDto);
}
