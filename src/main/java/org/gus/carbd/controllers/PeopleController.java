package org.gus.carbd.controllers;

import lombok.RequiredArgsConstructor;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.mapper.PersonDtoMapper;
import org.gus.carbd.mapper.VehicleDtoMapper;
import org.gus.carbd.service.PersonService;
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
@RequiredArgsConstructor
@RequestMapping(value = "/people")
//http://localhost:8080/people

public class PeopleController {

    public final PersonService personService;
    public final PersonDtoMapper personDTOMapper;
    public final VehicleDtoMapper vehicleDTOMapper;

    @GetMapping()
    public List<PersonDto> getPeopleList() {
        List<Person> peopleList = personService.getPeopleList();
        return personDTOMapper.toPersonDtoList(peopleList);
    }

    @GetMapping("/{id}")
    public PersonDto getPersonById(@PathVariable("id") int id) {
        Person person = personService.getPersonById(id);
        return personDTOMapper.toPersonDto(person);
    }

    @PostMapping("/add")
    public void addPerson(@RequestBody PersonDto personDTO) {
        personService.addPerson(personDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePersonById(id);
    }

    @PatchMapping("/{id}/edit")
    public void editPersonById(@PathVariable("id") int id, @RequestBody PersonDto changedPerson) {
        personService.editPersonById(id, changedPerson);
    }

    @GetMapping("/{id}/vehicles")
    public Set<VehicleDto> getPersonVehiclesByPersonId(@PathVariable("id") int id) {
        Set<Vehicle> vehicleSet = personService.getPersonVehiclesByPersonId(id);
        return vehicleDTOMapper.toVehicleDtoSet(vehicleSet);
    }

    @PostMapping("/{id}/vehicles/{vin}")
    public void updatePersonAssignVehicle(@PathVariable("id") int id,
                                          @PathVariable("vin") int vin) {
        personService.updatePersonAssignVehicle(id, vin);
    }

    @PatchMapping("/{id}/vehicles/{vin}")
    public void updatePersonUnAssignVehicle(@PathVariable("id") int id,
                                            @PathVariable("vin") int vin) {
        personService.updatePersonUnAssignVehicle(id, vin);
    }

    @GetMapping("/search/{passport}")
    public PersonDto getPersonByPassport(@PathVariable("passport") String passport) {
        Person person = personService.getPersonByPassport(passport);
        return personDTOMapper.toPersonDto(person);
    }

    @GetMapping("/search/{passport}/vehicles")
    public Set<VehicleDto> getPersonVehiclesByPassport(@PathVariable("passport") String passport) {
        Set<Vehicle> vehicleSet = personService.getPersonVehiclesByPassport(passport);
        return vehicleDTOMapper.toVehicleDtoSet(vehicleSet);
    }
}
