package org.gus.carbd.controllers;

import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PersonDTO;
import org.gus.carbd.dto.VehicleDTO;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.mapper.PersonDTOMapper;
import org.gus.carbd.mapper.VehicleDTOMapper;
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
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/vehicles")
//http://localhost:8080/vehicles

public class VehicleController {

    public final VehicleService vehicleService;
    public final VehicleDTOMapper vehicleDTOMapper;
    public final PersonDTOMapper personDTOMapper;

    @GetMapping()
    public List<VehicleDTO> getVehiclesList() {
        List<Vehicle> vehicleList = vehicleService.getVehiclesList();
        return vehicleList.stream().map(vehicleDTOMapper::toVehicleDTO).collect(Collectors.toList());
    }

    @GetMapping("/{vin}")
    public VehicleDTO getVehicleByVin(@PathVariable("vin") int vin) {
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        return vehicleDTOMapper.toVehicleDTO(vehicle);
    }

    @PostMapping("/add")
    public void addVehicle(@RequestBody Vehicle vehicle) {
        vehicleService.addVehicle(vehicle);
    }

    @DeleteMapping("/{vin}")
    public void deleteVehicleByVin(@PathVariable("vin") int vin) {
        vehicleService.deleteVehicleById(vin);
    }

    @PatchMapping("/{vin}/edit")
    public void editVehicleByVin(@PathVariable("vin") int vin, @RequestBody Vehicle changedVehicle) {
        vehicleService.editVehicleByVin(vin, changedVehicle);
    }

    @GetMapping("/{vin}/people")
    public Set<PersonDTO> getVehicleOwners(@PathVariable("vin") int vin) {
        Set<Person> personSet = vehicleService.getVehicleOwners(vin);
        return personSet.stream().map(personDTOMapper::toPersonDTO).collect(Collectors.toSet());
    }

    @GetMapping("/{vin}/peoplepass")
    public List<String> getVehicleOwnersPassports(@PathVariable("vin") int vin) {
        return vehicleService.getVehicleOwnersPassports(vin);
    }
}
