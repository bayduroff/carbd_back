package org.gus.carbd.controllers;

import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.mapper.PersonDtoMapper;
import org.gus.carbd.mapper.VehicleDtoMapper;
import org.gus.carbd.service.VehicleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vehicles")
//http://localhost:8080/vehicles

public class VehicleController {

    public final VehicleService vehicleService;
    public final VehicleDtoMapper vehicleDTOMapper;
    public final PersonDtoMapper personDTOMapper;

    @GetMapping()
    public List<VehicleDto> getVehiclesList() {
        List<Vehicle> vehicleList = vehicleService.getVehiclesList();
        return vehicleDTOMapper.toVehicleDtoList(vehicleList);
    }

    @GetMapping("/{vin}")
    public VehicleDto getVehicleByVin(@PathVariable("vin") int vin) {
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        return vehicleDTOMapper.toVehicleDto(vehicle);
    }

    @PostMapping("/add")
    public void addVehicle(@RequestBody VehicleDto vehicleDto) {
        vehicleService.addVehicle(vehicleDto);
    }

    @DeleteMapping("/{vin}")
    public void deleteVehicleByVin(@PathVariable("vin") int vin) {
        vehicleService.deleteVehicleByVin(vin);
    }

    @PatchMapping("/{vin}/edit")
    public void editVehicleByVin(@PathVariable("vin") int vin, @RequestBody VehicleDto changedVehicleDto) {
        vehicleService.editVehicleByVin(vin, changedVehicleDto);
    }

    @GetMapping("/{vin}/people")
    public Set<PersonDto> getVehicleOwners(@PathVariable("vin") int vin) {
        Set<Person> personSet = vehicleService.getVehicleOwners(vin);
        return personDTOMapper.toPersonDtoSet(personSet);
    }

    @GetMapping("/{vin}/peoplepass")
    public List<String> getVehicleOwnersPassports(@PathVariable("vin") int vin) {
        return vehicleService.getVehicleOwnersPassports(vin);
    }
}
