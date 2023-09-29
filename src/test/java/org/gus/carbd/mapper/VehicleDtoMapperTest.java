package org.gus.carbd.mapper;

import org.gus.carbd.dto.VehicleDto;
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
class VehicleDtoMapperTest {

    @InjectMocks
    private VehicleDtoMapperImpl vehicleDtoMapper;

    @Test
    void toVehicleDtoAllDataTest() {
        Set<Person> personSet = preparePersonSet();
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123, personSet);
        var result = vehicleDtoMapper.toVehicleDto(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
        assertEquals(personSet, result.getPeople());
    }

    @Test
    void toVehicleDtoPartOfDataTest() {
        Vehicle vehicle = new Vehicle(1, null, "test1", 123, null);
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
        Set<Person> personSet = preparePersonSet();
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
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123, null);
        Vehicle vehicle2 = new Vehicle(2, "test2", "test2", 321, preparePersonSet());
        List<Vehicle> vehicleList = List.of(vehicle, vehicle2);

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
        var personList = vehicleSet.stream().sorted(Comparator.comparing(Vehicle::getVin)).toList();
        assertLists(personList, resultList);
    }

    @Test
    void toVehicleDtoSetNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDtoSet(null));
    }

    @Test
    void updateVehicleFullChangeTest() {
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123, null);
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
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123, Collections.emptySet());
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
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123, Collections.emptySet());
        VehicleDto changedDto = new VehicleDto();
        vehicleDtoMapper.updateVehicle(vehicle, changedDto);
        assertEquals(1, vehicle.getVin());
        assertEquals("test", vehicle.getBrand());
        assertEquals("test1", vehicle.getModel());
        assertEquals(123, vehicle.getYear());
        assertEquals(Collections.emptySet(), vehicle.getPeople());
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

    private void assertLists(List<Vehicle> vehicleList, List<VehicleDto> vehicleDtoList) {
        for (int i = 0; i < vehicleDtoList.size(); i++) {
            assertEquals(vehicleList.get(i).getVin(), vehicleDtoList.get(i).getVin());
            assertEquals(vehicleList.get(i).getBrand(), vehicleDtoList.get(i).getBrand());
            assertEquals(vehicleList.get(i).getModel(), vehicleDtoList.get(i).getModel());
            assertEquals(vehicleList.get(i).getYear(), vehicleDtoList.get(i).getYear());
            assertEquals(vehicleList.get(i).getPeople(), vehicleDtoList.get(i).getPeople());
        }
    }
}