package org.gus.carbd.controllers;

import lombok.RequiredArgsConstructor;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/people")
//http://localhost:8080/people

public class PersonController {

    public final PersonService personService;
    public final PersonDtoMapper personDtoMapper;
    public final VehicleDtoMapper vehicleDtoMapper;

    @GetMapping()
    public List<PersonDto> getPeopleList() {
        List<Person> peopleList = personService.getPeopleList();
        return personDtoMapper.toPersonDtoList(peopleList);
    }

    @GetMapping("/{id}")
    public PersonDto getPersonById(@PathVariable("id") int id) {
        Person person = personService.getPersonById(id);
        return personDtoMapper.toPersonDto(person);
    }

    @PostMapping("/add")
    public void addPerson(@RequestBody PersonDto personDto) {
        Person person = personDtoMapper.toPerson(personDto);
        personService.addPerson(person);
    }

    @DeleteMapping("/{id}")
    public void deletePersonById(@PathVariable("id") int id) {
        personService.deletePersonById(id);
    }

    @PatchMapping("/{id}/edit")
    public void editPersonById(@PathVariable("id") int id, @RequestBody PersonDto changedPersonDto) {
        Person changedPerson = personDtoMapper.toPerson(changedPersonDto);
        personService.editPersonById(id, changedPerson);
    }

    @GetMapping("/{id}/vehicles")
    public List<VehicleDto> getPersonVehiclesByPersonId(@PathVariable("id") int id) {
        List<Vehicle> vehicleList = personService.getPersonVehiclesByPersonId(id);
        return vehicleDtoMapper.toVehicleDtoList(vehicleList);
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

    @GetMapping("/search")
    public PersonDto getPersonByPassport(@RequestParam String series,
                                         @RequestParam String number) {
        Person person = personService.getPersonByPassport(series, number);
        return personDtoMapper.toPersonDto(person);
    }

    @GetMapping("/search/vehicles")
    public List<VehicleDto> getPersonVehiclesByPassport(@RequestParam String series,
                                                        @RequestParam String number) {
        List<Vehicle> vehicleList = personService.getPersonVehiclesByPassport(series, number);
        return vehicleDtoMapper.toVehicleDtoList(vehicleList);
    }
}
