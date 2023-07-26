package org.gus.carbd.controllers;

import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.service.PersonService;
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
@RequestMapping(value = "/people")
//http://localhost:8080/people

public class PeopleController {

    public final PersonService personService;

    @Autowired
    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public List<Person> getPeopleList() {
        return personService.getPeopleList();
    }

    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable("id") int id) {
        return personService.getPersonById(id);
    }

    @PostMapping("/add")
    public void addPerson(@RequestBody Person person) {
        personService.addPerson(person);
    }

    @DeleteMapping("/{id}")
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePersonById(id);
    }

    @PatchMapping("/{id}/edit")
    public void editPersonById(@PathVariable("id") int id, @RequestBody Person changedPerson) {
        personService.editPersonById(id, changedPerson);
    }

    @GetMapping("/{id}/vehicles")
    public Set<Vehicle> getPersonVehiclesByPersonId(@PathVariable("id") int id) {
        return personService.getPersonVehiclesByPersonId(id);
    }

    //Put?
    @PostMapping("/{id}/vehicles/{vin}")
    public void assignVehicleToPerson(@PathVariable("id") int id,
                                      @PathVariable("vin") int vin) {
        personService.assignVehicleToPerson(id, vin);
    }

    //delete?
    @PatchMapping("/{id}/vehicles/{vin}")
    public void unAssignVehicleFromPerson(@PathVariable("id") int id,
                                          @PathVariable("vin") int vin) {
        personService.unAssignVehicleFromPerson(id, vin);
    }

    @GetMapping("/search/{passport}")
    public Person getPersonByPassport(@PathVariable("passport") String passport) throws Exception {
        return personService.getPersonByPassport(passport);
    }

    @GetMapping("/search/{passport}/vehicles")
    public Set<Vehicle> getPersonVehiclesByPassport(@PathVariable("passport") String passport) throws Exception {
        return personService.getPersonVehiclesByPassport(passport);
    }


}
