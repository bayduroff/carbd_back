package org.gus.carbd.mapper;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.entity.Passport;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PassportDtoMapper {

    PassportDto toPassportDto(Passport passport);

    Passport toPassport(PassportDto passportDto);

    void updatePassport(@MappingTarget Passport passport, PassportDto changedPassportDto);
}
