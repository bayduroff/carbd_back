package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface VehicleDtoMapper {

    VehicleDto toVehicleDto(Vehicle vehicle);

    Vehicle toVehicle(VehicleDto vehicleDto);

    List<VehicleDto> toVehicleDtoList(List<Vehicle> vehicles);

    Set<VehicleDto> toVehicleDtoSet(Set<Vehicle> vehicles);

    //не обращаться к trans методу изнутри PersonDtoMapper
    void updateVehicle(@MappingTarget Vehicle vehicle, VehicleDto changedVehicleDto);
}
