package org.gus.carbd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDomainMapper;
import org.gus.carbd.mapper.VehicleDomainMapper;
import org.gus.carbd.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehicleService {

    public final VehicleRepository vehicleRepository;
    private final VehicleDomainMapper vehicleDomainMapper;
    private final PersonDomainMapper personDomainMapper;


    public List<Vehicle> getVehiclesList() {
        var vehicleEntities = vehicleRepository.findAll();
        return vehicleDomainMapper.toVehicleList(vehicleEntities);
    }

    public VehicleEntity getVehicleEntityByVin(int vin) {
        Optional<VehicleEntity> result = vehicleRepository.findById(vin);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Did not find vehicle vin - " + vin);
        }
    }

    public Vehicle getVehicleByVin(int vin) {
        return vehicleDomainMapper.toVehicle(getVehicleEntityByVin(vin));
    }

    public void addVehicle(Vehicle vehicle) {
        VehicleEntity vehicleEntity = vehicleDomainMapper.toVehicleEntity(vehicle);
        vehicleRepository.save(vehicleEntity);
    }

    public void deleteVehicleByVin(int vin) {
        vehicleRepository.deleteById(vin);
    }

    @Transactional
    public void editVehicleByVin(int vin, Vehicle changedVehicle) {
        vehicleDomainMapper.updateVehicleEntity(getVehicleEntityByVin(vin), changedVehicle);
    }

    public List<Person> getVehicleOwners(int vin) {
        List<PersonEntity> personEntityList = getPeopleList(getVehicleEntityByVin(vin));
        var peopleList = personDomainMapper.toPersonList(personEntityList);
        if (peopleList == null || peopleList.isEmpty() || peopleList.stream().noneMatch(Objects::nonNull)) {
            throw new RuntimeException("There is no owners for vehicle with vin - " + vin);
        }
        return peopleList;
    }

    public List<Passport> getVehicleOwnersPassports(int vin) {
        var peopleList = getVehicleOwners(vin);
        List<Passport> passportList = new ArrayList<>();
        for (Person person : peopleList) {
            if (person != null) {
                passportList.add(person.getPassport());
            }
        }
        return passportList;
    }

    protected List<PersonEntity> getPeopleList(VehicleEntity vehicleEntity) {
        return vehicleEntity.getPeople() != null ? vehicleEntity.getPeople().stream().toList() : null;
    }
}
