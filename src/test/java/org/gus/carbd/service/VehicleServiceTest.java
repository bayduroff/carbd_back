package org.gus.carbd.service;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.exception.ResourceNotFoundException;
import org.gus.carbd.mapper.PassportDtoMapperImpl;
import org.gus.carbd.mapper.VehicleDtoMapperImpl;
import org.gus.carbd.repository.VehicleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @InjectMocks
    private VehicleService vehicleService;

    @Mock
    private VehicleRepository vehicleRepositoryMock;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private VehicleDtoMapperImpl vehicleDtoMapperImplMock;

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    private PassportDtoMapperImpl passportDtoMapperMock;

    @Test
    void getVehiclesListTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        List<VehicleEntity> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);

        doReturn(vehicleList).when(vehicleRepositoryMock).findAll();

        assertEquals(vehicleList, vehicleService.getVehiclesList());
    }

    @Test
    void getVehicleByVinPositiveTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "BMW", "X5",
                1990, null);
        doReturn(Optional.of(vehicle)).when(vehicleRepositoryMock).findById(any());

        assertEquals(vehicle, vehicleService.getVehicleByVin(1));
    }

    @Test
    void getVehicleByVinNegativeTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleByVin(1));
    }

    @Test
    void addVehiclePositiveTest() {
        ArgumentCaptor<VehicleEntity> vehicleCaptor = ArgumentCaptor.forClass(VehicleEntity.class);
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setBrand("Test");
        vehicleDto.setYear(2000);

        vehicleService.addVehicle(vehicleDto);
        verify(vehicleDtoMapperImplMock).toVehicle(vehicleDto);
        verify(vehicleRepositoryMock).save(vehicleCaptor.capture());
        assertEquals("Test", vehicleCaptor.getValue().getBrand());
        assertEquals(2000, vehicleCaptor.getValue().getYear());
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
        VehicleDto changedVehicleDto = new VehicleDto();
        changedVehicleDto.setYear(1990);
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setBrand("Test");
        vehicle.setYear(2000);

        doReturn(Optional.of(vehicle)).when(vehicleRepositoryMock).findById(any());

        vehicleService.editVehicleByVin(1, changedVehicleDto);
        assertEquals("Test", vehicle.getBrand());
        assertEquals(1990, vehicle.getYear());
    }

    @Test
    void editVehicleByVinNegativeTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.editVehicleByVin(1, new VehicleDto()));
        verify(vehicleDtoMapperImplMock, never()).updateVehicle(any(), any());
    }

    @Test
    void getVehicleOwnersPositiveTest() {
        PersonEntity person1 = new PersonEntity();
        person1.setName("First");
        PersonEntity person2 = new PersonEntity();
        person2.setName("Second");
        HashSet<PersonEntity> people = new HashSet<>();
        people.add(person1);
        people.add(person2);
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setPeople(people);

        doReturn(Optional.of(vehicle)).when(vehicleRepositoryMock).findById(any());

        assertEquals(people, vehicleService.getVehicleOwners(1));
    }

    @Test
    void getVehicleOwnersNegativeTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleOwners(1));
    }

    @Test
    void getVehicleOwnersPassportsPositiveTest() {
        PassportEntity passport1 = new PassportEntity();
        passport1.setNumber("123");
        passport1.setSeries("456");
        PassportEntity passport2 = new PassportEntity();
        passport2.setNumber("789");
        passport2.setSeries("098");

        PersonEntity person1 = new PersonEntity();
        person1.setId(1);
        person1.setPassport(passport1);
        PersonEntity person2 = new PersonEntity();
        person2.setId(2);
        person2.setPassport(passport2);

        HashSet<PersonEntity> people = new HashSet<>();
        people.add(person1);
        people.add(person2);
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setPeople(people);


        List<PassportDto> expectedResult = List.of(passportDtoMapperMock.toPassportDto(passport1),
                passportDtoMapperMock.toPassportDto(passport2));

        doReturn(Optional.of(vehicle)).when(vehicleRepositoryMock).findById(any());
        assertEquals(expectedResult, vehicleService.getVehicleOwnersPassports(1));
    }

    @Test
    void getVehicleOwnersPassportsVehicleNotFoundTest() {
        doReturn(Optional.empty()).when(vehicleRepositoryMock).findById(any());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.getVehicleOwnersPassports(1));
    }

    @Test
    void getVehicleOwnersPassportsVehicleOwnersEmptyOrNullTest() {
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setPeople(Collections.emptySet());

        doReturn(Optional.of(vehicle)).when(vehicleRepositoryMock).findById(any());
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleOwnersPassports(1));

        doReturn(Optional.of(new VehicleEntity())).when(vehicleRepositoryMock).findById(any());
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicleOwnersPassports(1));
    }
}