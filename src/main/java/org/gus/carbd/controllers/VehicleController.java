package org.gus.carbd.controllers;

import lombok.RequiredArgsConstructor;
import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.mapper.PassportDtoMapper;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vehicles")
//http://localhost:8080/vehicles

public class VehicleController {

    public final VehicleService vehicleService;
    public final VehicleDtoMapper vehicleDtoMapper;
    public final PersonDtoMapper personDtoMapper;
    public final PassportDtoMapper passportDtoMapper;

    @GetMapping()
    public List<VehicleDto> getVehiclesList() {
        List<Vehicle> vehicleList = vehicleService.getVehiclesList();
        return vehicleDtoMapper.toVehicleDtoList(vehicleList);
    }

    @GetMapping("/{vin}")
    public VehicleDto getVehicleByVin(@PathVariable("vin") int vin) {
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        return vehicleDtoMapper.toVehicleDto(vehicle);
    }

    @PostMapping("/add")
    public void addVehicle(@RequestBody VehicleDto vehicleDto) {
        Vehicle vehicle = vehicleDtoMapper.toVehicle(vehicleDto);
        vehicleService.addVehicle(vehicle);
    }

    @DeleteMapping("/{vin}")
    public void deleteVehicleByVin(@PathVariable("vin") int vin) {
        vehicleService.deleteVehicleByVin(vin);
    }

    @PatchMapping("/{vin}/edit")
    public void editVehicleByVin(@PathVariable("vin") int vin, @RequestBody VehicleDto changedVehicleDto) {
        Vehicle changedVehicle = vehicleDtoMapper.toVehicle(changedVehicleDto);
        vehicleService.editVehicleByVin(vin, changedVehicle);
    }

    @GetMapping("/{vin}/people")
    public List<PersonDto> getVehicleOwners(@PathVariable("vin") int vin) {
        List<Person> peopleList = vehicleService.getVehicleOwners(vin);
        return personDtoMapper.toPersonDtoList(peopleList);
    }

    @GetMapping("/{vin}/peoplepass")
    public List<PassportDto> getVehicleOwnersPassports(@PathVariable("vin") int vin) {
        List<Passport> passports = vehicleService.getVehicleOwnersPassports(vin);
        return passportDtoMapper.toPassportDtoList(passports);
    }
}
