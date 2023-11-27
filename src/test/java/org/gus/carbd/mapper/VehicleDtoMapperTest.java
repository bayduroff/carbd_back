package org.gus.carbd.mapper;

import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.dto.VehicleDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class VehicleDtoMapperTest {

    @InjectMocks
    private VehicleDtoMapperImpl vehicleDtoMapper;

    @Test
    void toVehicleDtoAllDataTest() {
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123);

        var result = vehicleDtoMapper.toVehicleDto(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
    }

    @Test
    void toVehicleDtoPartOfDataTest() {
        Vehicle vehicle = new Vehicle(1, null, "test1", 123);

        var result = vehicleDtoMapper.toVehicleDto(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
        assertNull(result.getBrand());
    }

    @Test
    void toVehicleDtoNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDto(null));
    }

    @Test
    void toVehicleAllDataTest() {
        VehicleDto vehicleDto = new VehicleDto(1, "test", "test1", 123);

        var result = vehicleDtoMapper.toVehicle(vehicleDto);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
    }

    @Test
    void toVehicleNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicle(null));
    }


    @Test
    void toVehicleDtoListAllDataTest() {
        Vehicle vehicle1 = new Vehicle(1, "test", "test1", 123);
        Vehicle vehicle2 = new Vehicle(2, "test2", "test2", 321);
        List<Vehicle> vehicleList = List.of(vehicle1, vehicle2);

        var result = vehicleDtoMapper.toVehicleDtoList(vehicleList);

        assertEquals(vehicleList.size(), result.size());
        assertLists(vehicleList, result);
    }

    @Test
    void toVehicleDtoListEmptyListTest() {
        var result = vehicleDtoMapper.toVehicleDtoList(Collections.emptyList());

        assertTrue(result.isEmpty());
    }

    @Test
    void toVehicleDtoListNullDataTest() {
        assertNull(vehicleDtoMapper.toVehicleDtoList(null));
    }

    private void assertLists(List<Vehicle> vehicleList, List<VehicleDto> vehicleDtoList) {
        for (int i = 0; i < vehicleDtoList.size(); i++) {
            assertEquals(vehicleList.get(i).getVin(), vehicleDtoList.get(i).getVin());
            assertEquals(vehicleList.get(i).getBrand(), vehicleDtoList.get(i).getBrand());
            assertEquals(vehicleList.get(i).getModel(), vehicleDtoList.get(i).getModel());
            assertEquals(vehicleList.get(i).getYear(), vehicleDtoList.get(i).getYear());
        }
    }
}