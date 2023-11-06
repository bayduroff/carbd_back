package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.dto.PassportDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface PassportDtoMapper {

    PassportDto toPassportDto(Passport passport);

    List<PassportDto> toPassportDtoList(List<Passport> passports);

    Passport toPassport(PassportDto passportDto);
}
