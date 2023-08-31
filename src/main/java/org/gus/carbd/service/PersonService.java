package org.gus.carbd.service;

import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.repository.PersonRepository;
import org.gus.carbd.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PersonService {

    @Autowired
    //@Qualifier("personRepository")
    private PersonRepository personRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Person> getPeopleList() {
        return personRepository.findAll();
    }

    public Person getPersonById(int id) {
        Optional<Person> result = personRepository.findById(id);
        Person person;
        if (result.isPresent()) {
            person = result.get();
        } else throw new ResourceNotFoundException("Did not find person id - " + id);

        return person;
    }

    public void addPerson(Person person) {
        var allPeople = personRepository.findAll();
        for (Person basePerson : allPeople) {
            if (basePerson.getPassport().equals(person.getPassport()))
                throw new RuntimeException("Person with passport: " + person.getPassport() + " already exists");
        }

        personRepository.save(person);
    }

    public void deletePersonById(int id) {
        personRepository.deleteById(id);
    }

    public void editPersonById(int id, Person changedPerson) {
        Optional<Person> result = personRepository.findById(id);
        Person person;
        if (result.isPresent()) {
            person = result.get();
        } else throw new ResourceNotFoundException("Did not find person id - " + id);

        if (changedPerson.getName() != null) {
            person.setName(changedPerson.getName());
        }

        if (changedPerson.getSurname() != null) {
            person.setSurname(changedPerson.getSurname());
        }

        if (changedPerson.getPatronymic() != null) {
            person.setPatronymic(changedPerson.getPatronymic());
        }

        if (changedPerson.getPassport() != null) {
            var allPeople = personRepository.findAll();
            for (Person basePerson : allPeople) {
                if (basePerson.getPassport().equals(changedPerson.getPassport()))
                    throw new RuntimeException("Person with passport: " + person.getPassport() + " already exists");
            }
            person.setPassport(changedPerson.getPassport());
        }

        if (changedPerson.getVehicles() != null) {
            person.setVehicles(changedPerson.getVehicles());
        }

        personRepository.save(person);
    }

    public void assignVehicleToPerson(int id, int vin) {
        Optional<Person> resultPerson = personRepository.findById(id);
        Person person;
        if (resultPerson.isPresent()) {
            person = resultPerson.get();
        } else throw new ResourceNotFoundException("Did not find person id - " + id);

        Optional<Vehicle> resultVehicle = vehicleRepository.findById(vin);
        Vehicle vehicle;
        if (resultVehicle.isPresent()) {
            vehicle = resultVehicle.get();
        } else throw new ResourceNotFoundException("Did not find vehicle vin - " + vin);

        person.getVehicles().add(vehicle);
        personRepository.save(person);
    }

    public void unAssignVehicleFromPerson(int id, int vin) {
        Optional<Person> resultPerson = personRepository.findById(id);
        Person person;
        if (resultPerson.isPresent()) {
            person = resultPerson.get();
        } else throw new ResourceNotFoundException("Did not find person id - " + id);

        Optional<Vehicle> resultVehicle = vehicleRepository.findById(vin);
        Vehicle vehicle;
        if (resultVehicle.isPresent()) {
            vehicle = resultVehicle.get();
        } else throw new ResourceNotFoundException("Did not find vehicle vin - " + vin);

        person.getVehicles().remove(vehicle);
        personRepository.save(person);
    }

    public Set<Vehicle> getPersonVehiclesByPersonId(int id) {
        Optional<Person> resultPerson = personRepository.findById(id);
        Person person;
        if (resultPerson.isPresent()) {
            person = resultPerson.get();
        } else throw new ResourceNotFoundException("Did not find person id - " + id);

        return person.getVehicles();
    }

    public Set<Vehicle> getPersonVehiclesByPassport(String passport) throws Exception {
        List<Person> allPeople = personRepository.findAll();
        for (Person person : allPeople) {
            if (passport.equals(person.getPassport())) {
                return person.getVehicles();
            }
        }
        throw new Exception("No person with such passport");
    }

    public Person getPersonByPassport(String passport) throws Exception {
        List<Person> allPeople = personRepository.findAll();
        for (Person person : allPeople) {
            if (passport.equals(person.getPassport())) {
                return person;
            }
        }
        throw new Exception("No person with such passport");
    }
}
