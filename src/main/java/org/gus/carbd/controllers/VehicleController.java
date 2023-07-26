package org.gus.carbd.controllers;

import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/vehicles")
//http://localhost:8080/vehicles

public class VehicleController {

    public final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping()
    public List<Vehicle> getVehiclesList() {
        return vehicleService.getVehiclesList();
    }

    @GetMapping("/{vin}")
    public Vehicle getVehicleByVin(@PathVariable("vin") int vin) {
        return vehicleService.getVehicleByVin(vin);
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
    public Set<Person> getVehicleOwners(@PathVariable("vin") int vin) {
        return vehicleService.getVehicleOwners(vin);
    }

    @GetMapping("/{vin}/peoplepass")
    public List<String> getVehicleOwnersPassports(@PathVariable("vin") int vin) {
        return vehicleService.getVehicleOwnersPassports(vin);
    }
}
