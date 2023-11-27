package org.gus.carbd.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDomainMapperImpl;
import org.gus.carbd.mapper.VehicleDomainMapperImpl;
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
    private final PersonDomainMapperImpl personDomainMapper;
    private final VehicleDomainMapperImpl vehicleDomainMapper;

    public PersonEntity getPersonEntityById(int id) {
        Optional<PersonEntity> result = personRepository.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with id - " + id);
        }
    }

    public PersonEntity getPersonEntityByPassport(String series, String number) {
        Optional<PersonEntity> resultPerson = personRepository.findPersonByPassportSeriesAndPassportNumber(series, number);
        if (resultPerson.isPresent()) {
            return resultPerson.get();
        } else {
            throw new ResourceNotFoundException("Did not find person with passport - " + series + number);
        }
    }

    public List<Person> getPeopleList() {
        var personEntities = personRepository.findAll();
        return personDomainMapper.toPersonList(personEntities);
    }

    public Person getPersonById(int id) {
        return personDomainMapper.toPerson(getPersonEntityById(id));
    }

    public void addPerson(Person person) {
        if (personWithPassportExistsInBase(person)) {
            throw new RuntimeException("Person with passport: " + person.getPassport().getSeries()
                    + person.getPassport().getNumber() + " already exists");
        }
        PersonEntity personEntity = personDomainMapper.toPersonEntity(person);
        personRepository.save(personEntity);
    }

    public void deletePersonById(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void editPersonById(int id, Person changedPerson) {
        if (personWithPassportExistsInBase(changedPerson)) {
            throw new RuntimeException("Person with passport: " + changedPerson.getPassport().getSeries()
                    + changedPerson.getPassport().getNumber() + " already exists");
        }

        personDomainMapper.updatePersonEntity(getPersonEntityById(id), changedPerson);
    }

    public List<Vehicle> getPersonVehiclesByPersonId(int id) {
        List<VehicleEntity> vehicleEntityList = getVehiclesList(getPersonEntityById(id));

        return vehicleDomainMapper.toVehicleList(vehicleEntityList);
    }

    public void updatePersonAssignVehicle(int id, int vin) {
        PersonEntity personEntity = getPersonEntityById(id);
        VehicleEntity vehicleEntity = vehicleService.getVehicleEntityByVin(vin);
        if (personEntity.getVehicles() != null) {
            personEntity.getVehicles().add(vehicleEntity);
        } else {
            Set<VehicleEntity> vehicles = new HashSet<>();
            vehicles.add(vehicleEntity);
            personEntity.setVehicles(vehicles);
        }

        personRepository.save(personEntity);
    }

    public void updatePersonUnAssignVehicle(int id, int vin) {
        PersonEntity personEntity = getPersonEntityById(id);
        if (personEntity.getVehicles() == null) {
            throw new RuntimeException("Person with id - " + id + " has no vehicles to unassign");
        }
        VehicleEntity vehicleEntity = vehicleService.getVehicleEntityByVin(vin);
        personEntity.getVehicles().remove(vehicleEntity);

        personRepository.save(personEntity);
    }

    public Person getPersonByPassport(String series, String number) {
        return personDomainMapper.toPerson(getPersonEntityByPassport(series, number));
    }

    public List<Vehicle> getPersonVehiclesByPassport(String series, String number) {
        List<VehicleEntity> vehicleEntityList = getVehiclesList(getPersonEntityByPassport(series, number));

        return vehicleDomainMapper.toVehicleList(vehicleEntityList);
    }

    protected boolean personWithPassportExistsInBase(Person person) {
        return person.getPassport() != null &&
                personRepository.existsByPassportSeriesAndPassportNumber(person.getPassport().getSeries(),
                        person.getPassport().getNumber());
    }

    protected List<VehicleEntity> getVehiclesList(PersonEntity personEntity) {
        return personEntity.getVehicles() != null ? personEntity.getVehicles().stream().toList() : null;
    }
}
