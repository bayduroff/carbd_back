package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.entity.PassportEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface PassportDomainMapper {

    Passport toPassport(PassportEntity passportEntity);

    @Mapping(target = "person", ignore = true)
    PassportEntity toPassportEntity(Passport passport);

    @Mapping(target = "person", ignore = true)
    void updatePassportEntity(@MappingTarget PassportEntity passport, Passport changedPassport);
}
