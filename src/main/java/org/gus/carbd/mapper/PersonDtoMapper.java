package org.gus.carbd.mapper;

import org.gus.carbd.domain.Person;
import org.gus.carbd.dto.PersonDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = PassportDtoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR, unmappedSourcePolicy = ReportingPolicy.WARN)
public interface PersonDtoMapper {

    @Mapping(target = "passportDto", source = "passport")
    PersonDto toPersonDto(Person person);

    List<PersonDto> toPersonDtoList(List<Person> people);

    @Mapping(target = "passport", source = "passportDto")
    Person toPerson(PersonDto personDTO);
}
