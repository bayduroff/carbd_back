package org.gus.carbd.mapper;

import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.VehicleEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        unmappedSourcePolicy = ReportingPolicy.WARN)
public interface VehicleDomainMapper {

    Vehicle toVehicle(VehicleEntity vehicleEntity);

    List<Vehicle> toVehicleList(List<VehicleEntity> vehicles);

    @Mapping(target = "people", ignore = true)
    VehicleEntity toVehicleEntity(Vehicle vehicle);

    @Mapping(target = "people", ignore = true)
    void updateVehicleEntity(@MappingTarget VehicleEntity vehicleEntity, Vehicle changedVehicle);
}
