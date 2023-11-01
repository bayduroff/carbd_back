package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
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
class VehicleDtoMapperTest {

    @InjectMocks
    private VehicleDtoMapperImpl vehicleDtoMapper;

    @Test
    void toVehicleDtoAllDataTest() {
        Set<PersonEntity> personSet = preparePersonSet();
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, personSet);
        var result = vehicleDtoMapper.toVehicleDto(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
        assertEquals(personSet, result.getPeople());
    }

    @Test
    void toVehicleDtoPartOfDataTest() {
        VehicleEntity vehicle = new VehicleEntity(1, null, "test1", 123, null);
        var result = vehicleDtoMapper.toVehicleDto(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
        assertNull(result.getBrand());
        assertNull(result.getPeople());
    }

    @Test
    void toVehicleDtoNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDto(null));
    }


    @Test
    void toVehicleAllDataTest() {
        Set<PersonEntity> personSet = preparePersonSet();
        VehicleDto vehicleDto = new VehicleDto(1, "test", "test1", 123, personSet);
        var result = vehicleDtoMapper.toVehicle(vehicleDto);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
        assertEquals(personSet, result.getPeople());
    }

    @Test
    void toVehicleNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicle(null));
    }

    @Test
    void toVehicleDtoListAllDataTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, null);
        VehicleEntity vehicle2 = new VehicleEntity(2, "test2", "test2", 321, preparePersonSet());
        List<VehicleEntity> vehicleList = List.of(vehicle, vehicle2);

        var result = vehicleDtoMapper.toVehicleDtoList(vehicleList);

        assertEquals(vehicleList.size(), result.size());
        assertLists(vehicleList, result);
    }

    @Test
    void toVehicleDtoListNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDtoList(null));
    }

    @Test
    void toVehicleDtoSetAllDataTest() {
        var vehicleSet = prepareVehicleSet();
        var result = vehicleDtoMapper.toVehicleDtoSet(vehicleSet);

        assertEquals(vehicleSet.size(), result.size());
        var resultList = result.stream().sorted(Comparator.comparing(VehicleDto::getVin)).toList();
        var personList = vehicleSet.stream().sorted(Comparator.comparing(VehicleEntity::getVin)).toList();
        assertLists(personList, resultList);
    }

    @Test
    void toVehicleDtoSetNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDtoSet(null));
    }

    @Test
    void updateVehicleFullChangeTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, null);
        VehicleDto changedDto = new VehicleDto(2, "changeTest", "changeTest1", 321,
                Collections.emptySet());
        vehicleDtoMapper.updateVehicle(vehicle, changedDto);
        assertEquals(2, vehicle.getVin());
        assertEquals("changeTest", vehicle.getBrand());
        assertEquals("changeTest1", vehicle.getModel());
        assertEquals(321, vehicle.getYear());
        assertEquals(Collections.emptySet(), vehicle.getPeople());
    }

    @Test
    void updateVehiclePartOfDataChangeTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        VehicleDto changedDto = new VehicleDto(null, "changeTest", null, 321,
                null);
        vehicleDtoMapper.updateVehicle(vehicle, changedDto);
        assertEquals(1, vehicle.getVin());
        assertEquals("changeTest", vehicle.getBrand());
        assertEquals("test1", vehicle.getModel());
        assertEquals(321, vehicle.getYear());
        assertEquals(Collections.emptySet(), vehicle.getPeople());
    }

    @Test
    void updateVehicleNoChangeTest() {
        VehicleEntity vehicle = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        VehicleDto changedDto = new VehicleDto();
        vehicleDtoMapper.updateVehicle(vehicle, changedDto);
        assertEquals(1, vehicle.getVin());
        assertEquals("test", vehicle.getBrand());
        assertEquals("test1", vehicle.getModel());
        assertEquals(123, vehicle.getYear());
        assertEquals(Collections.emptySet(), vehicle.getPeople());
    }

    private Set<VehicleEntity> prepareVehicleSet() {
        VehicleEntity vehicle1 = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        VehicleEntity vehicle2 = new VehicleEntity(2, "test2", "test2", 321, Collections.emptySet());
        Set<VehicleEntity> vehicleSet = new HashSet<>();
        vehicleSet.add(vehicle1);
        vehicleSet.add(vehicle2);
        return vehicleSet;
    }

    private Set<PersonEntity> preparePersonSet() {
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        PersonEntity person2 = new PersonEntity(2, "Test2", "Testov2",
                "Testovich2", new PassportEntity(), null);
        Set<PersonEntity> personSet = new HashSet<>();
        personSet.add(person);
        personSet.add(person2);
        return personSet;
    }

    private void assertLists(List<VehicleEntity> vehicleList, List<VehicleDto> vehicleDtoList) {
        for (int i = 0; i < vehicleDtoList.size(); i++) {
            assertEquals(vehicleList.get(i).getVin(), vehicleDtoList.get(i).getVin());
            assertEquals(vehicleList.get(i).getBrand(), vehicleDtoList.get(i).getBrand());
            assertEquals(vehicleList.get(i).getModel(), vehicleDtoList.get(i).getModel());
            assertEquals(vehicleList.get(i).getYear(), vehicleDtoList.get(i).getYear());
            assertEquals(vehicleList.get(i).getPeople(), vehicleDtoList.get(i).getPeople());
        }
    }
}