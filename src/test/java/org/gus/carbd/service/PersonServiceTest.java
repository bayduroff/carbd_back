package org.gus.carbd.service;

import org.gus.carbd.dto.PersonDTO;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDtoMapperImpl;
import org.gus.carbd.repository.PersonRepository;
import org.gus.carbd.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepositoryMock;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private PersonDtoMapperImpl personDtoMapperImplMock;

    @Mock
    private VehicleService vehicleServiceMock;

    @Mock
    private VehicleRepository vehicleRepositoryMock;


    @Test
    void getPeopleListTest() {
        List<Person> people = new ArrayList<>();
        Person person = new Person(1, "12345", "Test",
                "Testov", "Testovich", null);
        people.add(person);

        doReturn(people).when(personRepositoryMock).findAll();

        assertEquals(people, personService.getPeopleList());
    }

    @Test
    void getPersonByIdPositiveTest() {
        Person person = new Person(1, "12345", "Test",
                "Testov", "Testovich", null);
        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());

        assertEquals(person, personService.getPersonById(1));
    }

    @Test
    void getPersonByIdNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> personService.getPersonById(1));
    }

    @Test
    void addPersonPositiveTest() {
        ArgumentCaptor<String> passportCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        doReturn(false).when(personRepositoryMock).existsByPassport(any());
        PersonDTO personDTO = new PersonDTO();
        personDTO.setPassport("12345");
        personDTO.setName("Test");

        personService.addPerson(personDTO);
        verify(personRepositoryMock).existsByPassport(passportCaptor.capture());
        assertEquals("12345", passportCaptor.getValue());
        verify(personDtoMapperImplMock).toPerson(personDTO);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals("12345", personCaptor.getValue().getPassport());
        assertEquals("Test", personCaptor.getValue().getName());
    }

    @Test
    void addPersonNegativeTest() {
        doReturn(true).when(personRepositoryMock).existsByPassport(any());

        assertThrows(RuntimeException.class, () -> personService.addPerson(new PersonDTO()));
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void deletePersonByIdTest() {
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        personService.deletePersonById(1);
        verify(personRepositoryMock).deleteById(idCaptor.capture());
        assertEquals(1, idCaptor.getValue());
    }

    @Test
    void editPersonByIdPositiveTest() {
        PersonDTO changedPersonDto = new PersonDTO();
        changedPersonDto.setPassport("54321");
        Person person = new Person();
        person.setPassport("12345");
        person.setName("Test");
        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());

        personService.editPersonById(1, changedPersonDto);
        assertEquals("54321", person.getPassport());
        assertEquals("Test", person.getName());
    }

    @Test
    void editPersonByIdNegativeTest() {
        doReturn(true).when(personRepositoryMock).existsByPassport(any());

        assertThrows(RuntimeException.class, () -> personService.editPersonById(1, new PersonDTO()));
        verify(personDtoMapperImplMock, never()).updatePerson(any(), any());
    }

    @Test
    void updatePersonAssignVehicleNoPersonFoundTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("Did not find person with id - 1", exception.getMessage());
        verify(vehicleServiceMock, never()).getVehicleByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonAssignVehicleNoVehicleFoundTest() {
        doReturn(Optional.of(new Person())).when(personRepositoryMock).findById(any());
        doThrow(new ResourceNotFoundException("No vehicles")).when(vehicleServiceMock).getVehicleByVin(anyInt());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("No vehicles", exception.getMessage());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonAssignVehiclePersonAlreadyHasVehiclesTest() {
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Vehicle baseVehicle = new Vehicle();
        baseVehicle.setBrand("BMW");
        Vehicle assigningVehicle = new Vehicle();
        assigningVehicle.setBrand("Lada");
        HashSet<Vehicle> vehicles = new HashSet<>();
        vehicles.add(baseVehicle);
        Person person = new Person();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());
        doReturn(assigningVehicle).when(vehicleServiceMock).getVehicleByVin(anyInt());

        personService.updatePersonAssignVehicle(1, 1);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals(2, personCaptor.getValue().getVehicles().size());
        assertTrue(personCaptor.getValue().getVehicles().contains(baseVehicle));
        assertTrue(personCaptor.getValue().getVehicles().contains(assigningVehicle));
    }

    @Test
    void updatePersonAssignVehiclePersonVehiclesNullTest() {
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Vehicle assigningVehicle = new Vehicle();
        assigningVehicle.setBrand("Lada");

        doReturn(Optional.of(new Person())).when(personRepositoryMock).findById(any());
        doReturn(assigningVehicle).when(vehicleServiceMock).getVehicleByVin(anyInt());

        personService.updatePersonAssignVehicle(1, 1);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals(1, personCaptor.getValue().getVehicles().size());
        assertTrue(personCaptor.getValue().getVehicles().contains(assigningVehicle));
    }

    @Test
    void updatePersonUnAssignVehicleNoPersonFoundTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonUnAssignVehicle(1, 1));
        assertEquals("Did not find person with id - 1", exception.getMessage());
        verify(vehicleServiceMock, never()).getVehicleByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehiclePersonVehiclesNullTest() {
        doReturn(Optional.of(new Person())).when(personRepositoryMock).findById(any());

        var exception = assertThrows(RuntimeException.class,
                () -> personService.updatePersonUnAssignVehicle(1, 1));
        assertEquals("Person with id - 1 has no vehicles to unassign", exception.getMessage());
        verify(vehicleServiceMock, never()).getVehicleByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehicleNoVehicleFoundTest() {
        Person person = new Person();
        person.setVehicles(new HashSet<>());

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());
        doThrow(new ResourceNotFoundException("No vehicles")).when(vehicleServiceMock).getVehicleByVin(anyInt());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("No vehicles", exception.getMessage());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehiclePersonHasVehiclesPositiveTest() {
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        Vehicle baseVehicle1 = new Vehicle();
        baseVehicle1.setBrand("BMW");
        Vehicle baseVehicle2 = new Vehicle();
        baseVehicle2.setBrand("Lada");
        HashSet<Vehicle> vehicles = new HashSet<>();
        vehicles.add(baseVehicle1);
        vehicles.add(baseVehicle2);
        Person person = new Person();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());
        doReturn(baseVehicle2).when(vehicleServiceMock).getVehicleByVin(anyInt());

        personService.updatePersonUnAssignVehicle(1, 1);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals(1, personCaptor.getValue().getVehicles().size());
        assertTrue(personCaptor.getValue().getVehicles().contains(baseVehicle1));
    }

    @Test
    void getPersonVehiclesByPersonIdPositiveTest() {
        Vehicle baseVehicle = new Vehicle();
        baseVehicle.setBrand("Lada");
        HashSet<Vehicle> vehicles = new HashSet<>();
        vehicles.add(baseVehicle);
        Person person = new Person();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());

        var result = personService.getPersonVehiclesByPersonId(1);
        assertEquals(vehicles, result);
    }

    @Test
    void getPersonVehiclesByPersonIdNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> personService.getPersonVehiclesByPersonId(1));
    }

    @Test
    void getPersonByPassportPositiveTest() {
        Person person = new Person();
        person.setName("Test");
        doReturn(Optional.of(person)).when(personRepositoryMock).findByPassport(anyString());

        var result = personService.getPersonByPassport("123");
        assertEquals(person, result);
    }

    @Test
    void getPersonByPassportNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findByPassport(anyString());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonByPassport("123"));
        assertEquals("Did not find person with passport - 123", exception.getMessage());
    }

    @Test
    void getPersonVehiclesByPassportPositiveTest() {
        Vehicle baseVehicle = new Vehicle();
        baseVehicle.setBrand("Lada");
        HashSet<Vehicle> vehicles = new HashSet<>();
        vehicles.add(baseVehicle);
        Person person = new Person();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findByPassport(anyString());

        var result = personService.getPersonVehiclesByPassport("123");
        assertEquals(vehicles, result);
    }

    @Test
    void getPersonVehiclesByPassportNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findByPassport(anyString());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonVehiclesByPassport("123"));
        assertEquals("Did not find person with passport - 123", exception.getMessage());
    }
}