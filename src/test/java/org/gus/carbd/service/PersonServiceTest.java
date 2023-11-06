package org.gus.carbd.service;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDomainMapperImpl;
import org.gus.carbd.mapper.VehicleDomainMapperImpl;
import org.gus.carbd.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
    @Mock
    private VehicleService vehicleServiceMock;
    @Mock
    private PersonDomainMapperImpl personDomainMapperMock;
    @Mock
    private VehicleDomainMapperImpl vehicleDomainMapperMock;

    @Test
    void getPersonEntityByIdPositiveTest() {
        PersonEntity personEntity = new PersonEntity(1, "Test",
                "Testov", "Testovich", new PassportEntity(), null);
        doReturn(Optional.of(personEntity)).when(personRepositoryMock).findById(any());

        assertEntities(personEntity, personService.getPersonEntityById(1));
    }

    @Test
    void getPersonEntityByIdNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> personService.getPersonEntityById(1));
    }

    @Test
    void getPersonEntityByPassportPositiveTest() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Test");

        doReturn(Optional.of(personEntity)).when(personRepositoryMock)
                .findPersonByPassportSeriesAndPassportNumber(anyString(), anyString());

        var result = personService.getPersonEntityByPassport("123", "456");
        assertEntities(personEntity, result);
    }

    @Test
    void getPersonEntityByPassportNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock)
                .findPersonByPassportSeriesAndPassportNumber(anyString(), anyString());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonEntityByPassport("123", "456"));
        assertEquals("Did not find person with passport - 123456", exception.getMessage());
    }

    @Test
    void getPeopleListTest() {
        List<Person> people = new ArrayList<>();
        Person person = new Person(1, "Test",
                "Testov", "Testovich", new Passport());
        people.add(person);

        doReturn(new ArrayList<PersonEntity>()).when(personRepositoryMock).findAll();
        doReturn(people).when(personDomainMapperMock).toPersonList(anyList());
        assertEquals(people, personService.getPeopleList());
    }

    @Test
    void getPersonByIdTest() {
        Person person = new Person(1, "Test",
                "Testov", "Testovich", new Passport());

        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock).findById(any());
        doReturn(person).when(personDomainMapperMock).toPerson(any());

        assertEquals(person, personService.getPersonById(1));
    }

    @Test
    void addPersonPositiveTest() {
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        Person person = new Person();
        person.setPassport(new Passport(1, "1111", "2222"));
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Test");

        doReturn(false).when(personRepositoryMock)
                .existsByPassportSeriesAndPassportNumber(anyString(), anyString());
        doReturn(personEntity).when(personDomainMapperMock).toPersonEntity(person);

        personService.addPerson(person);
        verify(personRepositoryMock).existsByPassportSeriesAndPassportNumber(stringCaptor.capture(),
                stringCaptor.capture());
        var capturedArgs = stringCaptor.getAllValues();
        assertEquals("1111", capturedArgs.get(0));
        assertEquals("2222", capturedArgs.get(1));
        verify(personRepositoryMock).save(personEntityCaptor.capture());
        assertEquals("Test", personEntityCaptor.getValue().getName());
    }

    @Test
    void addPersonHasNoPassportTest() {
        ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        Person person = new Person();
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Test");

        doReturn(personEntity).when(personDomainMapperMock).toPersonEntity(person);

        personService.addPerson(person);
        verify(personRepositoryMock, never()).existsByPassportSeriesAndPassportNumber(anyString(), anyString());
        verify(personRepositoryMock).save(personEntityCaptor.capture());
        assertEquals("Test", personEntityCaptor.getValue().getName());
    }

    @Test
    void addPersonPassportExistsInDbTest() {
        Person person = new Person();
        person.setPassport(new Passport());

        doReturn(true).when(personRepositoryMock)
                .existsByPassportSeriesAndPassportNumber(anyString(), anyString());

        assertThrows(RuntimeException.class, () -> personService.addPerson(person));
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
        ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Test");
        Person changedPerson = new Person();
        changedPerson.setPassport(new Passport());

        doReturn(Optional.of(personEntity)).when(personRepositoryMock).findById(any());
        doReturn(false).when(personRepositoryMock)
                .existsByPassportSeriesAndPassportNumber(any(), any());
        doNothing().when(personDomainMapperMock).updatePersonEntity(any(), any());

        personService.editPersonById(1, changedPerson);
        verify(personDomainMapperMock).updatePersonEntity(personEntityCaptor.capture(), personCaptor.capture());
        assertEquals(personEntity.getName(), personEntityCaptor.getValue().getName());
        assertEquals(changedPerson, personCaptor.getValue());
    }

    @Test
    void editPersonByIdHasNoPassportNoPersonWithIdTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> personService.editPersonById(1, new Person()));
        verify(personDomainMapperMock, never()).updatePersonEntity(any(), any());
    }

    @Test
    void editPersonByIdHasNoPassportFindPersonWithIdTest() {
        ArgumentCaptor<PersonEntity> personEntityCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setName("Test");
        Person changedPerson = new Person();
        changedPerson.setName("Test2");

        doReturn(Optional.of(personEntity)).when(personRepositoryMock).findById(any());
        doNothing().when(personDomainMapperMock).updatePersonEntity(any(), any());

        personService.editPersonById(1, changedPerson);
        verify(personDomainMapperMock).updatePersonEntity(personEntityCaptor.capture(), personCaptor.capture());
        assertEquals(personEntity.getName(), personEntityCaptor.getValue().getName());
        assertEquals(changedPerson, personCaptor.getValue());
    }

    @Test
    void editPersonByIdPassportExistsInDbTest() {
        Person changedPerson = new Person();
        changedPerson.setPassport(new Passport());

        doReturn(true).when(personRepositoryMock)
                .existsByPassportSeriesAndPassportNumber(any(), any());

        assertThrows(RuntimeException.class, () -> personService.editPersonById(1, changedPerson));
        verify(personDomainMapperMock, never()).updatePersonEntity(any(), any());
    }

    @Test
    void getPersonVehiclesByPersonIdPositiveTest() {
        ArgumentCaptor<List<VehicleEntity>> listCaptor = ArgumentCaptor.forClass(List.class);

        VehicleEntity baseVehicleEntity = new VehicleEntity();
        baseVehicleEntity.setBrand("Lada");
        Set<VehicleEntity> vehicles = new HashSet<>();
        vehicles.add(baseVehicleEntity);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setVehicles(vehicles);

        Vehicle expectedVehicle = new Vehicle();
        expectedVehicle.setBrand("BMW");
        List<Vehicle> expectedVehicleList = new ArrayList<>();
        expectedVehicleList.add(expectedVehicle);

        doReturn(Optional.of(personEntity)).when(personRepositoryMock).findById(any());
        doReturn(expectedVehicleList).when(vehicleDomainMapperMock).toVehicleList(anyList());

        var result = personService.getPersonVehiclesByPersonId(1);
        verify(vehicleDomainMapperMock).toVehicleList(listCaptor.capture());
        assertSetAndList(vehicles, listCaptor.getValue());
        assertEquals(expectedVehicle.getBrand(), result.get(0).getBrand());
    }

    @Test
    void getPersonVehiclesByPersonIdVehiclesNullTest() {
        ArgumentCaptor<List<VehicleEntity>> listCaptor = ArgumentCaptor.forClass(List.class);

        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock).findById(any());
        doReturn(null).when(vehicleDomainMapperMock).toVehicleList(any());

        assertNull(personService.getPersonVehiclesByPersonId(1));
        verify(vehicleDomainMapperMock).toVehicleList(listCaptor.capture());
        assertNull(listCaptor.getValue());
    }

    @Test
    void getPersonVehiclesByPersonIdNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> personService.getPersonVehiclesByPersonId(1));
        verify(vehicleDomainMapperMock, never()).toVehicleList(anyList());
    }

    @Test
    void updatePersonAssignVehicleNoPersonFoundTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findById(any());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("Did not find person with id - 1", exception.getMessage());
        verify(vehicleServiceMock, never()).getVehicleEntityByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonAssignVehicleNoVehicleFoundTest() {
        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock).findById(any());
        doThrow(new ResourceNotFoundException("No vehicles")).when(vehicleServiceMock).getVehicleEntityByVin(anyInt());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("No vehicles", exception.getMessage());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonAssignVehiclePersonAlreadyHasVehiclesTest() {
        ArgumentCaptor<PersonEntity> personCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        VehicleEntity baseVehicle = new VehicleEntity(1, "BMW", null, null, null);
        VehicleEntity assigningVehicle = new VehicleEntity(2, "Lada", null, null, null);
        HashSet<VehicleEntity> vehicles = new HashSet<>();
        vehicles.add(baseVehicle);
        PersonEntity person = new PersonEntity();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());
        doReturn(assigningVehicle).when(vehicleServiceMock).getVehicleEntityByVin(anyInt());

        personService.updatePersonAssignVehicle(1, 1);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals(2, personCaptor.getValue().getVehicles().size());
        assertTrue(personCaptor.getValue().getVehicles().contains(baseVehicle));
        assertTrue(personCaptor.getValue().getVehicles().contains(assigningVehicle));
    }

    @Test
    void updatePersonAssignVehiclePersonVehiclesNullTest() {
        ArgumentCaptor<PersonEntity> personCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        VehicleEntity assigningVehicle = new VehicleEntity();
        assigningVehicle.setBrand("Lada");

        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock).findById(any());
        doReturn(assigningVehicle).when(vehicleServiceMock).getVehicleEntityByVin(anyInt());

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
        verify(vehicleServiceMock, never()).getVehicleEntityByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehiclePersonVehiclesNullTest() {
        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock).findById(any());

        var exception = assertThrows(RuntimeException.class,
                () -> personService.updatePersonUnAssignVehicle(1, 1));
        assertEquals("Person with id - 1 has no vehicles to unassign", exception.getMessage());
        verify(vehicleServiceMock, never()).getVehicleEntityByVin(anyInt());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehicleNoVehicleFoundTest() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setVehicles(new HashSet<>());

        doReturn(Optional.of(personEntity)).when(personRepositoryMock).findById(any());
        doThrow(new ResourceNotFoundException("No vehicles")).when(vehicleServiceMock).getVehicleEntityByVin(anyInt());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.updatePersonAssignVehicle(1, 1));
        assertEquals("No vehicles", exception.getMessage());
        verify(personRepositoryMock, never()).save(any());
    }

    @Test
    void updatePersonUnAssignVehiclePersonHasVehiclesPositiveTest() {
        ArgumentCaptor<PersonEntity> personCaptor = ArgumentCaptor.forClass(PersonEntity.class);
        VehicleEntity baseVehicle1 = new VehicleEntity(1, "BMW", null, null, null);
        VehicleEntity baseVehicle2 = new VehicleEntity(2, "Lada", null, null, null);
        HashSet<VehicleEntity> vehicles = new HashSet<>();
        vehicles.add(baseVehicle1);
        vehicles.add(baseVehicle2);
        PersonEntity person = new PersonEntity();
        person.setVehicles(vehicles);

        doReturn(Optional.of(person)).when(personRepositoryMock).findById(any());
        doReturn(baseVehicle2).when(vehicleServiceMock).getVehicleEntityByVin(anyInt());

        personService.updatePersonUnAssignVehicle(1, 1);
        verify(personRepositoryMock).save(personCaptor.capture());
        assertEquals(1, personCaptor.getValue().getVehicles().size());
        assertTrue(personCaptor.getValue().getVehicles().contains(baseVehicle1));
    }

    @Test
    void getPersonByPassportTest() {
        Person person = new Person(1, "Test",
                "Testov", "Testovich", new Passport());
        doReturn(Optional.of(new PersonEntity()))
                .when(personRepositoryMock).findPersonByPassportSeriesAndPassportNumber(any(), any());
        doReturn(person).when(personDomainMapperMock).toPerson(any());

        assertEquals(person, personService.getPersonByPassport("1111", "2222"));
    }

    @Test
    void getPersonVehiclesByPassportPositiveTest() {
        ArgumentCaptor<List<VehicleEntity>> listCaptor = ArgumentCaptor.forClass(List.class);

        VehicleEntity baseVehicle = new VehicleEntity();
        baseVehicle.setBrand("Lada");
        HashSet<VehicleEntity> vehicles = new HashSet<>();
        vehicles.add(baseVehicle);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setVehicles(vehicles);

        Vehicle expectedVehicle = new Vehicle();
        expectedVehicle.setBrand("BMW");
        List<Vehicle> expectedVehicleList = new ArrayList<>();
        expectedVehicleList.add(expectedVehicle);

        doReturn(Optional.of(personEntity)).when(personRepositoryMock)
                .findPersonByPassportSeriesAndPassportNumber(anyString(), anyString());
        doReturn(expectedVehicleList).when(vehicleDomainMapperMock).toVehicleList(anyList());

        var result = personService.getPersonVehiclesByPassport("123", "456");
        verify(vehicleDomainMapperMock).toVehicleList(listCaptor.capture());
        assertSetAndList(vehicles, listCaptor.getValue());
        assertEquals(expectedVehicle.getBrand(), result.get(0).getBrand());
    }

    @Test
    void getPersonVehiclesByPassportVehiclesNullTest() {
        ArgumentCaptor<List<VehicleEntity>> listCaptor = ArgumentCaptor.forClass(List.class);

        doReturn(Optional.of(new PersonEntity())).when(personRepositoryMock)
                .findPersonByPassportSeriesAndPassportNumber(anyString(), anyString());
        doReturn(null).when(vehicleDomainMapperMock).toVehicleList(any());

        assertNull(personService.getPersonVehiclesByPassport("123", "456"));
        verify(vehicleDomainMapperMock).toVehicleList(listCaptor.capture());
        assertNull(listCaptor.getValue());
    }

    @Test
    void getPersonVehiclesByPassportNegativeTest() {
        doReturn(Optional.empty()).when(personRepositoryMock).findPersonByPassportSeriesAndPassportNumber(anyString(), anyString());

        var exception = assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonVehiclesByPassport("123", "456"));
        assertEquals("Did not find person with passport - 123456", exception.getMessage());
        verify(vehicleDomainMapperMock, never()).toVehicleList(anyList());
    }

    @Test
    void personWithPassportExistsInBaseTrue() {
        Person person = new Person();
        person.setPassport(new Passport());

        doReturn(true).when(personRepositoryMock).existsByPassportSeriesAndPassportNumber(any(), any());

        assertTrue(personService.personWithPassportExistsInBase(person));
    }

    @Test
    void personWithPassportExistsInBaseFalse() {
        Person person = new Person();
        person.setPassport(new Passport());

        doReturn(false).when(personRepositoryMock).existsByPassportSeriesAndPassportNumber(any(), any());

        assertFalse(personService.personWithPassportExistsInBase(person));
    }

    @Test
    void personWithPassportExistsInBasePassportNull() {
        assertFalse(personService.personWithPassportExistsInBase(new Person()));
    }

    @Test
    void getVehiclesListVehiclesExist() {
        PersonEntity personEntity = new PersonEntity();
        Set<VehicleEntity> vehicleEntitySet = new HashSet<>();
        VehicleEntity vehicleEntity1 = new VehicleEntity(1, "BMW", null, null, null);
        VehicleEntity vehicleEntity2 = new VehicleEntity(2, "Lada", null, null, null);
        vehicleEntitySet.add(vehicleEntity1);
        vehicleEntitySet.add(vehicleEntity2);
        personEntity.setVehicles(vehicleEntitySet);

        var result = personService.getVehiclesList(personEntity);
        assertTrue(vehicleEntitySet.containsAll(result));
        assertEquals(vehicleEntitySet.size(), result.size());
    }

    @Test
    void getVehiclesListVehiclesNull() {
        assertNull(personService.getVehiclesList(new PersonEntity()));
    }

    @Test
    void getVehiclesListVehiclesEmpty() {
        PersonEntity personEntity = new PersonEntity();
        personEntity.setVehicles(Collections.emptySet());

        assertTrue(personService.getVehiclesList(personEntity).isEmpty());
    }

    private void assertSetAndList(Set<VehicleEntity> vehicles, List<VehicleEntity> vehicleEntityList) {
        var vehiclesToListSorted = vehicles.stream()
                .sorted(Comparator.comparing(VehicleEntity::getVin)).toList();
        var vehicleEntityArrayList = new ArrayList<>(vehicleEntityList);
        vehicleEntityArrayList.sort(Comparator.comparing(VehicleEntity::getVin));

        assertEquals(vehiclesToListSorted.get(0).getVin(), vehicleEntityArrayList.get(0).getVin());
        assertEquals(vehiclesToListSorted.get(0).getBrand(), vehicleEntityArrayList.get(0).getBrand());
        assertEquals(vehiclesToListSorted.get(0).getModel(), vehicleEntityArrayList.get(0).getModel());
        assertEquals(vehiclesToListSorted.get(0).getYear(), vehicleEntityArrayList.get(0).getYear());
    }

    private void assertEntities(PersonEntity personEntity1, PersonEntity personEntity2) {
        assertEquals(personEntity1.getId(), personEntity2.getId());
        assertEquals(personEntity1.getName(), personEntity2.getName());
        assertEquals(personEntity1.getSurname(), personEntity2.getSurname());
        assertEquals(personEntity1.getPatronymic(), personEntity2.getPatronymic());
        assertEquals(personEntity1.getPassport(), personEntity2.getPassport());
        assertEquals(personEntity1.getVehicles(), personEntity2.getVehicles());
    }
}