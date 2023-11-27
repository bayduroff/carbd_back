package org.gus.carbd.mapper;

import org.gus.carbd.domain.Person;
import org.gus.carbd.entity.PersonEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = PassportDomainMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface PersonDomainMapper {

    Person toPerson(PersonEntity personEntity);

    List<Person> toPersonList(List<PersonEntity> personEntityList);

    @Mapping(target = "vehicles", ignore = true)
    PersonEntity toPersonEntity(Person person);

    @Mapping(target = "vehicles", ignore = true)
    void updatePersonEntity(@MappingTarget PersonEntity personEntity, Person changedPerson);

    @AfterMapping
    default void linkPersonWithPassport(@MappingTarget PersonEntity personEntity) {
        if (personEntity.getPassport() != null) {
            personEntity.getPassport().setPerson(personEntity);
        }
    }
}
