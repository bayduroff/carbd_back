package org.gus.carbd.service;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PersonDomainMapperImpl;
import org.gus.carbd.mapper.VehicleDomainMapperImpl;
import org.gus.carbd.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepositoryMock;
    @Mock
    private VehicleDomainMapperImpl vehicleDomainMapperImplMock;
    @Mock
    private PersonDomainMapperImpl personDomainMapperImplMock;

    @Test
    void getVehiclesListTest() {
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123);
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);

        doReturn(new ArrayList<>()).when(vehicleRepositoryMock).findAll();
        doReturn(vehicleList).when(vehicleDomainMapperImplMock).toVehicleList(anyList());

        assertEquals(vehicleList, vehicleService.getVehiclesList());
    }

    @Test
    void getVehicleEntityByVinPositiveTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "BMW", "X5", 1990, null);
        doReturn(Optional.of(vehicleEntity)).when(vehicleRepositoryMock).findById(any());

        assertEntities(vehicleEntity, vehicleService.getVehicleEntityByVin(1));
    }

    @Test
    void getVehicleEntityByVinVehicleNotFoundTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleEntityByVin(1));
    }

    @Test
    void getVehicleByVinTest() {
        Vehicle vehicle = new Vehicle(1, "brand", "model", 1234);

        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());
        doReturn(vehicle).when(vehicleDomainMapperImplMock).toVehicle(any());

        assertEquals(vehicle, vehicleService.getVehicleByVin(1));
    }

    @Test
    void addVehiclePositiveTest() {
        ArgumentCaptor<VehicleEntity> vehicleCaptor = ArgumentCaptor.forClass(VehicleEntity.class);
        doReturn(new VehicleEntity(1, "Brand", "Model", 1234, null))
                .when(vehicleDomainMapperImplMock).toVehicleEntity(any(Vehicle.class));

        vehicleService.addVehicle(new Vehicle());
        verify(vehicleRepositoryMock).save(vehicleCaptor.capture());
        assertEquals("Brand", vehicleCaptor.getValue().getBrand());
        assertEquals(1234, vehicleCaptor.getValue().getYear());
    }

    @Test
    void deleteVehicleByVinTest() {
        ArgumentCaptor<Integer> vinCaptor = ArgumentCaptor.forClass(Integer.class);

        vehicleService.deleteVehicleByVin(1);
        verify(vehicleRepositoryMock).deleteById(vinCaptor.capture());
        assertEquals(1, vinCaptor.getValue());
    }

    @Test
    void editVehicleByVinPositiveTest() {
        Vehicle changedVehicle = new Vehicle();
        changedVehicle.setYear(1990);
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setBrand("Test");
        vehicleEntity.setYear(2000);

        doReturn(Optional.of(vehicleEntity)).when(vehicleRepositoryMock).findById(any());
        doCallRealMethod().when(vehicleDomainMapperImplMock).updateVehicleEntity(any(), any());

        vehicleService.editVehicleByVin(1, changedVehicle);
        assertEquals("Test", vehicleEntity.getBrand());
        assertEquals(1990, vehicleEntity.getYear());
    }

    @Test
    void editVehicleByVinVehicleNotFoundTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.editVehicleByVin(1, new Vehicle()));
        verify(vehicleDomainMapperImplMock, never()).updateVehicleEntity(any(), any());
    }

    @Test
    void getVehicleOwnersPositiveTest() {
        List<Person> people = new ArrayList<>(List.of(new Person(), new Person()));

        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());
        doReturn(people).when(personDomainMapperImplMock).toPersonList(any());

        assertEquals(people, vehicleService.getVehicleOwners(1));
    }

    @Test
    void getVehicleOwnersVehicleNotFoundTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleOwners(1));
        verify(personDomainMapperImplMock, never()).toPersonList(any());
    }

    @Test
    void getVehicleOwnersNullOrEmptyOwnersTest() {
        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());

        doReturn(null).when(personDomainMapperImplMock).toPersonList(any());
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleOwners(1));

        doReturn(Collections.emptyList()).when(personDomainMapperImplMock).toPersonList(any());
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleOwners(1));
    }

    @Test
    void getVehicleOwnersNullOwnersInEntitySetTest() {
        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());

        /* Only null owners test */
        List<Person> personList = new ArrayList<>();
        personList.add(null);
        doReturn(personList).when(personDomainMapperImplMock).toPersonList(any());
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleOwners(1));

        /* Exists not null owner */
        personList.add(new Person());
        doReturn(personList).when(personDomainMapperImplMock).toPersonList(any());
        assertEquals(personList, vehicleService.getVehicleOwners(1));
    }

    @Test
    void getVehicleOwnersPassportsPositiveTest() {
        Passport passport1 = new Passport(1, "123", "456");
        Passport passport2 = new Passport(2, "789", "098");
        Person person1 = new Person(1, null, null, null, passport1);
        Person person2 = new Person(2, null, null, null, passport2);
        List<Person> people = new ArrayList<>(List.of(person1, person2));

        List<Passport> expectedResult = List.of(passport1, passport2);
        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());
        doReturn(people).when(personDomainMapperImplMock).toPersonList(any());

        var result = vehicleService.getVehicleOwnersPassports(1);
        assertTrue(result.containsAll(expectedResult));
        assertEquals(expectedResult.size(), result.size());
    }


    @Test
    void getVehicleOwnersPassportsVehicleNotFoundTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleOwnersPassports(1));
    }

    @Test
    void getVehicleOwnersPassportsVehicleOwnersSetHasNullOwnerTest() {
        Passport passport1 = new Passport(1, "123", "456");
        Person person1 = new Person(1, null, null, null, passport1);
        List<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(null);

        List<Passport> expectedResult = List.of(passport1);
        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());
        doReturn(people).when(personDomainMapperImplMock).toPersonList(any());

        var result = vehicleService.getVehicleOwnersPassports(1);
        assertTrue(result.containsAll(expectedResult));
        assertEquals(expectedResult.size(), result.size());
    }

    @Test
    void getPeopleListPeopleExist() {
        VehicleEntity vehicleEntity = new VehicleEntity();
        Set<PersonEntity> personEntitySet = new HashSet<>();
        PersonEntity personEntity1 =
                new PersonEntity(1, "Ivan", null, null, null, null);
        PersonEntity personEntity2 =
                new PersonEntity(2, "Marya", null, null, null, null);
        personEntitySet.add(personEntity1);
        personEntitySet.add(personEntity2);
        vehicleEntity.setPeople(personEntitySet);

        var result = vehicleService.getPeopleList(vehicleEntity);
        assertTrue(personEntitySet.containsAll(result));
        assertEquals(personEntitySet.size(), result.size());
    }

    @Test
    void getVehiclesListVehiclesNull() {
        assertNull(vehicleService.getPeopleList(new VehicleEntity()));
    }

    @Test
    void getVehiclesListVehiclesEmpty() {
        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setPeople(Collections.emptySet());

        assertTrue(vehicleService.getPeopleList(vehicleEntity).isEmpty());
    }

    private void assertEntities(VehicleEntity vehicleEntity1, VehicleEntity vehicleEntity2) {
        assertEquals(vehicleEntity1.getVin(), vehicleEntity2.getVin());
        assertEquals(vehicleEntity1.getBrand(), vehicleEntity2.getBrand());
        assertEquals(vehicleEntity1.getModel(), vehicleEntity2.getModel());
        assertEquals(vehicleEntity1.getYear(), vehicleEntity2.getYear());
        assertEquals(vehicleEntity1.getPeople(), vehicleEntity2.getPeople());
    }
}