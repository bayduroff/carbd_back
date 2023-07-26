package org.gus.carbd.service;

import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class VehicleService {
    @Autowired
    //@Qualifier("personRepository")
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getVehiclesList() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleByVin(int vin) {
        Optional<Vehicle> result = vehicleRepository.findById(vin);
        Vehicle vehicle = new Vehicle();

        if (result.isPresent()) {
            vehicle = result.get();
        }

        return vehicle;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicleById(int vin) {
        vehicleRepository.deleteById(vin);
    }

    public void editVehicleByVin(int vin, Vehicle changedVehicle) {
        Optional<Vehicle> result = vehicleRepository.findById(vin);

        Vehicle vehicle = new Vehicle();
        if (result.isPresent()) {
            vehicle = result.get();
        }

        if (changedVehicle.getBrand()!= null) {  //проверка на длину строки?
            vehicle.setBrand(changedVehicle.getBrand());
        }

        if (changedVehicle.getModel()!= null) {
            vehicle.setModel(changedVehicle.getModel());
        }

        if (changedVehicle.getYear()!= null) {
            vehicle.setYear(changedVehicle.getYear());
        }

        if (changedVehicle.getPeople()!= null) {
            vehicle.setPeople(vehicle.getPeople());
        }

        vehicleRepository.save(vehicle);
    }

    public Set<Person> getVehicleOwners(int vin) {
        Vehicle vehicle = vehicleRepository.findById(vin).get();
        return vehicle.getPeople();
    }

    public List<String> getVehicleOwnersPassports(int vin) {
        Vehicle vehicle = vehicleRepository.findById(vin).get();
        List<String> passportList = new ArrayList<>();
        var peopleSet = vehicle.getPeople();
        for (Person person: peopleSet){
            passportList.add(person.getPassport());
        }
        return passportList;
    }
}
