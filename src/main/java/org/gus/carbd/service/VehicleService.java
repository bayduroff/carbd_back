package org.gus.carbd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PassportDtoMapper;
import org.gus.carbd.mapper.VehicleDtoMapper;
import org.gus.carbd.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VehicleService {
    public final VehicleRepository vehicleRepository;

    private final VehicleDtoMapper vehicleDtoMapper;

    private final PassportDtoMapper passportDtoMapper;

    public List<Vehicle> getVehiclesList() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleByVin(int vin) {
        Optional<Vehicle> result = vehicleRepository.findById(vin);
        Vehicle vehicle;
        if (result.isPresent()) {
            vehicle = result.get();
        } else {
            throw new ResourceNotFoundException("Did not find vehicle vin - " + vin);
        }

        return vehicle;
    }

    public void addVehicle(VehicleDto vehicleDto) {
        vehicleRepository.save(vehicleDtoMapper.toVehicle(vehicleDto));
    }

    public void deleteVehicleByVin(int vin) {
        vehicleRepository.deleteById(vin);
    }

    @Transactional
    public void editVehicleByVin(int vin, VehicleDto changedVehicleDto) {
        vehicleDtoMapper.updateVehicle(getVehicleByVin(vin), changedVehicleDto);
    }

    public Set<Person> getVehicleOwners(int vin) {
        Vehicle vehicle = getVehicleByVin(vin);

        return vehicle.getPeople();
    }

    public List<PassportDto> getVehicleOwnersPassports(int vin) {
        var peopleSet = getVehicleOwners(vin);
        if (peopleSet == null || peopleSet.isEmpty()) {
            throw new RuntimeException("There is no owners for vehicle with vin - " + vin);
        }
        List<PassportDto> passportList = new ArrayList<>();
        for (Person person : peopleSet) {
            var passport = passportDtoMapper.toPassportDto(person.getPassport());
            passportList.add(passport);
        }

        return passportList;
    }
}
