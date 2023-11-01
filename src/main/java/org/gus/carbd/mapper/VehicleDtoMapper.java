package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.VehicleEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface VehicleDtoMapper {

    VehicleEntity toVehicle(VehicleDto vehicleDto);

    VehicleDto toVehicleDto(VehicleEntity vehicle);

    List<VehicleDto> toVehicleDtoList(List<VehicleEntity> vehicles);

    Set<VehicleDto> toVehicleDtoSet(Set<VehicleEntity> vehicles);

    void updateVehicle(@MappingTarget VehicleEntity vehicle, VehicleDto changedVehicleDto);
}
