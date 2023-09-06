package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDTO;
import org.gus.carbd.entity.Vehicle;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleDTOMapper {

    VehicleDTO toVehicleDTO(Vehicle vehicle);
}
