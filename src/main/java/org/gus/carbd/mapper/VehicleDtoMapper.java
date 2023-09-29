package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.Vehicle;
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

    Vehicle toVehicle(VehicleDto vehicleDto);

    VehicleDto toVehicleDto(Vehicle vehicle);

    List<VehicleDto> toVehicleDtoList(List<Vehicle> vehicles);

    Set<VehicleDto> toVehicleDtoSet(Set<Vehicle> vehicles);

    void updateVehicle(@MappingTarget Vehicle vehicle, VehicleDto changedVehicleDto);
}
