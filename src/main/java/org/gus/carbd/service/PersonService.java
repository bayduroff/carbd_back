package org.gus.carbd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDtoMapper;
import org.gus.carbd.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PersonService {

    public final PersonRepository personRepository;
    public final VehicleService vehicleService;
    private final PersonDtoMapper personDtoMapper;

    public List<PersonEntity> getPeopleList() {
        return personRepository.findAll();
        //тут маппер преобразует персонЭнтити в ПерсонДомен
        //после возвращает лист ПёрсонДомен
    }

    public PersonEntity getPersonById(int id) {
        Optional<PersonEntity> result = personRepository.findById(id);
        PersonEntity person;
        if (result.isPresent()) {
            person = result.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with id - " + id);
        }

        return person;
    }

    public void addPerson(PersonDto personDto) {
        if (personWithPassportExistsInBase(personDto)) {
            throw new RuntimeException("Person with passport: " + personDto.getPassportDto().getSeries()
                    + personDto.getPassportDto().getNumber() + " already exists");
        }

        personRepository.save(personDtoMapper.toPerson(personDto));
    }

    public void deletePersonById(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void editPersonById(int id, PersonDto changedPersonDto) {
        if (personWithPassportExistsInBase(changedPersonDto)) {
            throw new RuntimeException("Person with passport: " + changedPersonDto.getPassportDto().getSeries()
                    + changedPersonDto.getPassportDto().getNumber() + " already exists");
        }

        personDtoMapper.updatePerson(getPersonById(id), changedPersonDto);
    }

    public void updatePersonAssignVehicle(int id, int vin) {
        PersonEntity person = getPersonById(id);
        VehicleEntity vehicle = vehicleService.getVehicleByVin(vin);
        if (person.getVehicles() != null) {
            person.getVehicles().add(vehicle);
        } else {
            Set<VehicleEntity> vehicles = new HashSet<>();
            vehicles.add(vehicle);
            person.setVehicles(vehicles);
        }

        personRepository.save(person);
    }

    public void updatePersonUnAssignVehicle(int id, int vin) {
        PersonEntity person = getPersonById(id);
        if (person.getVehicles() == null) {
            throw new RuntimeException("Person with id - " + id + " has no vehicles to unassign");
        }
        VehicleEntity vehicle = vehicleService.getVehicleByVin(vin);
        person.getVehicles().remove(vehicle);

        personRepository.save(person);
    }

    public Set<VehicleEntity> getPersonVehiclesByPersonId(int id) {
        PersonEntity person = getPersonById(id);

        return person.getVehicles();
    }

    public PersonEntity getPersonByPassport(String series, String number) {
        Optional<PersonEntity> resultPerson = personRepository.findPersonByPassportSeriesAndPassportNumber(series, number);
        PersonEntity person;
        if (resultPerson.isPresent()) {
            person = resultPerson.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with passport - " + series + number);
        }

        return person;
    }

    public Set<VehicleEntity> getPersonVehiclesByPassport(String series, String number) {
        PersonEntity person = getPersonByPassport(series, number);

        return person.getVehicles();
    }

    private boolean personWithPassportExistsInBase(PersonDto personDto) {
        return personDto.getPassportDto() != null &&
                personRepository.existsByPassportSeriesAndPassportNumber(personDto.getPassportDto().getSeries(),
                        personDto.getPassportDto().getNumber());
    }
}
