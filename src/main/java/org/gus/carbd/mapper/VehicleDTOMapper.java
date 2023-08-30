package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDTO;
import org.gus.carbd.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleDTOMapper {

    @Mapping(target = "vin", source = "vin")
    @Mapping(target = "model", source = "model")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "people", source = "people")
    VehicleDTO toVehicleDTO(Vehicle vehicle);
}
