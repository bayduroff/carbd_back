package org.gus.carbd.mapper;

import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class PersonDtoMapperTest {

    @InjectMocks
    private PersonDtoMapperImpl personDtoMapper;

    @Test
    void toPersonDtoAllDataTest() {
        Set<Vehicle> vehicleSet = prepareVehicleSet();
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", vehicleSet);
        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals("12345", result.getPassport());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertEquals(vehicleSet, result.getVehicles());
    }

    @Test
    void toPersonDtoPartOfDataTest() {
        Person person = new Person(1, null, "Test", "Testov",
                null, null);
        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertNull(result.getPassport());
        assertNull(result.getPatronymic());
        assertNull(result.getVehicles());
    }

    @Test
    void toPersonDtoNullDataTest() {
        assertNull(personDtoMapper.toPersonDto(null));
    }

    @Test
    void toPersonAllDataTest() {
        Set<Vehicle> vehicleSet = prepareVehicleSet();
        PersonDto personDto = new PersonDto(1, "12345", "Test", "Testov",
                "Testovich", vehicleSet);
        var result = personDtoMapper.toPerson(personDto);
        assertEquals(1, result.getId());
        assertEquals("12345", result.getPassport());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertEquals(vehicleSet, result.getVehicles());
    }

    @Test
    void toPersonNullDataTest() {
        assertNull(personDtoMapper.toPerson(null));
    }

    @Test
    void toPersonDtoListAllDataTest() {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", null);
        Person person2 = new Person(2, "54321", "Test2", "Testov2",
                "Testovich2", null);
        List<Person> personList = List.of(person, person2);

        var result = personDtoMapper.toPersonDtoList(personList);

        assertEquals(personList.size(), result.size());
        assertLists(personList, result);
    }

    @Test
    void toPersonDtoListNullDataTest() {
        assertNull(personDtoMapper.toPersonDtoList(null));
    }

    @Test
    void toPersonDtoSetAllDataTest() {
        var personSet = preparePersonSet();
        var result = personDtoMapper.toPersonDtoSet(personSet);

        assertEquals(personSet.size(), result.size());
        var resultList = result.stream().sorted(Comparator.comparing(PersonDto::getId)).toList();
        var personList = personSet.stream().sorted(Comparator.comparing(Person::getId)).toList();
        assertLists(personList, resultList);
    }

    @Test
    void toPersonDtoSetNullDataTest() {
        assertNull(personDtoMapper.toPersonDtoSet(null));
    }

    @Test
    void updatePersonFullChangeTest() {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", null);
        PersonDto changedDto = new PersonDto(2, "54321", "ChangeTest", "ChangeTestov",
                "ChangeTestovich", Collections.emptySet());
        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(2, person.getId());
        assertEquals("54321", person.getPassport());
        assertEquals("ChangeTest", person.getName());
        assertEquals("ChangeTestov", person.getSurname());
        assertEquals("ChangeTestovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    @Test
    void updatePersonPartOfDataChangeTest() {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", Collections.emptySet());
        PersonDto changedDto = new PersonDto(null, "54321", null, "ChangeTestov",
                null, null);
        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(1, person.getId());
        assertEquals("54321", person.getPassport());
        assertEquals("Test", person.getName());
        assertEquals("ChangeTestov", person.getSurname());
        assertEquals("Testovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    @Test
    void updatePersonNoChangeTest() {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", Collections.emptySet());
        PersonDto changedDto = new PersonDto();
        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(1, person.getId());
        assertEquals("12345", person.getPassport());
        assertEquals("Test", person.getName());
        assertEquals("Testov", person.getSurname());
        assertEquals("Testovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    private void assertLists(List<Person> personList, List<PersonDto> personDtoList) {
        for (int i = 0; i < personDtoList.size(); i++) {
            assertEquals(personList.get(i).getId(), personDtoList.get(i).getId());
            assertEquals(personList.get(i).getName(), personDtoList.get(i).getName());
            assertEquals(personList.get(i).getPassport(), personDtoList.get(i).getPassport());
            assertEquals(personList.get(i).getSurname(), personDtoList.get(i).getSurname());
            assertEquals(personList.get(i).getPatronymic(), personDtoList.get(i).getPatronymic());
            assertEquals(personList.get(i).getVehicles(), personDtoList.get(i).getVehicles());
        }
    }

    private Set<Vehicle> prepareVehicleSet() {
        Vehicle vehicle1 = new Vehicle(1, "test", "test1", 123, Collections.emptySet());
        Vehicle vehicle2 = new Vehicle(2, "test2", "test2", 321, Collections.emptySet());
        Set<Vehicle> vehicleSet = new HashSet<>();
        vehicleSet.add(vehicle1);
        vehicleSet.add(vehicle2);
        return vehicleSet;
    }

    private Set<Person> preparePersonSet() {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", null);
        Person person2 = new Person(2, "54321", "Test2", "Testov2",
                "Testovich2", null);
        Set<Person> personSet = new HashSet<>();
        personSet.add(person);
        personSet.add(person2);
        return personSet;
    }
}