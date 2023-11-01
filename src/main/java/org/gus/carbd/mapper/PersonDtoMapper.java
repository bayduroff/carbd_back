package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.PersonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = PassportDtoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface PersonDtoMapper {

    @Mapping(target = "passportDto", source = "passport")
    PersonDto toPersonDto(PersonEntity person);

    List<PersonDto> toPersonDtoList(List<PersonEntity> people);

    Set<PersonDto> toPersonDtoSet(Set<PersonEntity> people);

    @Mapping(target = "passport", source = "passportDto")
    PersonEntity toPerson(PersonDto personDTO);

    @AfterMapping
    default void linkPersonWithPassport(@MappingTarget PersonEntity person) {
        person.getPassport().setPerson(person);
    }

    @Mapping(target = "passport", source = "passportDto")
    void updatePerson(@MappingTarget PersonEntity person, PersonDto changedPersonDto);
}
