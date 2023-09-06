package org.gus.carbd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PersonDTO;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDtoMapper;
import org.gus.carbd.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// ToDo: имеет ли смысл писать тесты на методы из репы

@Service
@RequiredArgsConstructor
public class PersonService {

    public final PersonRepository personRepository;
    public final VehicleService vehicleService;
    private final PersonDtoMapper personDtoMapper;

    public List<Person> getPeopleList() {
        return personRepository.findAll();
    }

    public Person getPersonById(int id) {
        Optional<Person> result = personRepository.findById(id);
        Person person;
        if (result.isPresent()) {
            person = result.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with id - " + id);
        }

        return person;
    }

    public void addPerson(PersonDTO personDTO) {
        if (personRepository.existsByPassport(personDTO.getPassport())) {
            throw new RuntimeException("Person with passport: " + personDTO.getPassport() + " already exists");
        }

        personRepository.save(personDtoMapper.toPerson(personDTO));
    }

    public void deletePersonById(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void editPersonById(int id, PersonDTO changedPersonDto) {
        if (personRepository.existsByPassport(changedPersonDto.getPassport())) {
            throw new RuntimeException("Person with passport: " + changedPersonDto.getPassport() + " already exists");
        }

        personDtoMapper.updatePerson(getPersonById(id), changedPersonDto);
    }

    public void updatePersonAssignVehicle(int id, int vin) {
        Person person = getPersonById(id);
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        if (person.getVehicles() != null) {
            person.getVehicles().add(vehicle);
        } else {
            Set<Vehicle> vehicles = new HashSet<>();
            vehicles.add(vehicle);
            person.setVehicles(vehicles);
        }

        personRepository.save(person);
    }

    public void updatePersonUnAssignVehicle(int id, int vin) {
        Person person = getPersonById(id);
        if (person.getVehicles() == null) {
            throw new RuntimeException("Person with id - " + id + " has no vehicles to unassign");
        }
        Vehicle vehicle = vehicleService.getVehicleByVin(vin);
        person.getVehicles().remove(vehicle);

        personRepository.save(person);
    }

    public Set<Vehicle> getPersonVehiclesByPersonId(int id) {
        Person person = getPersonById(id);

        return person.getVehicles();
    }

    public Person getPersonByPassport(String passport) {
        Optional<Person> resultPerson = personRepository.findByPassport(passport);
        Person person;
        if (resultPerson.isPresent()) {
            person = resultPerson.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with passport - " + passport);
        }

        return person;
    }

    public Set<Vehicle> getPersonVehiclesByPassport(String passport) {
        Person person = getPersonByPassport(passport);

        return person.getVehicles();
    }
}
