package org.gus.carbd.mapper;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.entity.PassportEntity;
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

    PassportDto toPassportDto(PassportEntity passport);

    @Mapping(target = "passport_id", ignore = true)
    @Mapping(target = "person", ignore = true)
    PassportEntity toPassport(PassportDto passportDto);

    @Mapping(target = "passport_id", ignore = true)
    @Mapping(target = "person", ignore = true)
    void updatePassport(@MappingTarget PassportEntity passport, PassportDto changedPassportDto);
}
