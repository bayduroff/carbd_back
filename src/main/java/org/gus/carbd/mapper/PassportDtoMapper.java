package org.gus.carbd.mapper;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.entity.Passport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface PassportDtoMapper {

    PassportDto toPassportDto(Passport passport);

    @Mapping(target = "passport_id", ignore = true)
    @Mapping(target = "person", ignore = true)
    Passport toPassport(PassportDto passportDto);

    @Mapping(target = "passport_id", ignore = true)
    @Mapping(target = "person", ignore = true)
    void updatePassport(@MappingTarget Passport passport, PassportDto changedPassportDto);
}
