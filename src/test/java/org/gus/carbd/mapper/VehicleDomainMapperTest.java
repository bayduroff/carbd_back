package org.gus.carbd.mapper;

import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class VehicleDomainMapperTest {

    @InjectMocks
    private VehicleDomainMapperImpl vehicleDomainMapper;

    @Test
    void toVehicleAllDataTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "test", "test1", 123, null);
        var result = vehicleDomainMapper.toVehicle(vehicleEntity);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
    }

    @Test
    void toVehiclePartOfDataTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, null, "test1", null, null);
        var result = vehicleDomainMapper.toVehicle(vehicleEntity);
        assertEquals(1, result.getVin());
        assertEquals("test1", result.getModel());
        assertNull(result.getYear());
        assertNull(result.getBrand());
    }

    @Test
    void toVehicleNullDataTest() {
        assertNull(vehicleDomainMapper.toVehicle(null));
    }

    @Test
    void toVehicleListAllDataTest() {
        VehicleEntity vehicleEntity1 = new VehicleEntity(1, "test", "test1", 123, null);
        VehicleEntity vehicleEntity2 = new VehicleEntity(2, "test2", "test2", 321, Collections.emptySet());
        List<VehicleEntity> vehicleEntityList = List.of(vehicleEntity1, vehicleEntity2);

        var result = vehicleDomainMapper.toVehicleList(vehicleEntityList);

        assertEquals(vehicleEntityList.size(), result.size());
        assertLists(vehicleEntityList, result);
    }

    @Test
    void toVehicleListNullDataTest() {
        assertNull(vehicleDomainMapper.toVehicleList(null));
    }

    @Test
    void toVehicleEntityAllDataTest() {
        Vehicle vehicle = new Vehicle(1, "test", "test1", 123);

        var result = vehicleDomainMapper.toVehicleEntity(vehicle);
        assertEquals(1, result.getVin());
        assertEquals("test", result.getBrand());
        assertEquals("test1", result.getModel());
        assertEquals(123, result.getYear());
    }

    @Test
    void toVehicleEntityNullDataTest() {
        assertNull(vehicleDomainMapper.toVehicleEntity(null));
    }

    @Test
    void updateVehicleFullChangeTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "test", "test1", 123, null);
        Vehicle changedVehicle = new Vehicle(2, "changeTest", "changeTest1", 321);
        vehicleDomainMapper.updateVehicleEntity(vehicleEntity, changedVehicle);
        assertEquals(2, vehicleEntity.getVin());
        assertEquals("changeTest", vehicleEntity.getBrand());
        assertEquals("changeTest1", vehicleEntity.getModel());
        assertEquals(321, vehicleEntity.getYear());
        assertNull(vehicleEntity.getPeople());
    }

    @Test
    void updateVehiclePartOfDataChangeTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "test", "test1", 123, null);
        Vehicle changedVehicle = new Vehicle(null, "changeTest", null, 321);
        vehicleDomainMapper.updateVehicleEntity(vehicleEntity, changedVehicle);
        assertEquals(1, vehicleEntity.getVin());
        assertEquals("changeTest", vehicleEntity.getBrand());
        assertEquals("test1", vehicleEntity.getModel());
        assertEquals(321, vehicleEntity.getYear());
        assertNull(vehicleEntity.getPeople());
    }

    @Test
    void updateVehicleNoChangeTest() {
        VehicleEntity vehicleEntity = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        Vehicle changedVehicle = new Vehicle();
        vehicleDomainMapper.updateVehicleEntity(vehicleEntity, changedVehicle);
        assertEquals(1, vehicleEntity.getVin());
        assertEquals("test", vehicleEntity.getBrand());
        assertEquals("test1", vehicleEntity.getModel());
        assertEquals(123, vehicleEntity.getYear());
        assertEquals(Collections.emptySet(), vehicleEntity.getPeople());
    }

    private void assertLists(List<VehicleEntity> vehicleEntityList, List<Vehicle> vehicleList) {
        for (int i = 0; i < vehicleList.size(); i++) {
            assertEquals(vehicleEntityList.get(i).getVin(), vehicleList.get(i).getVin());
            assertEquals(vehicleEntityList.get(i).getBrand(), vehicleList.get(i).getBrand());
            assertEquals(vehicleEntityList.get(i).getModel(), vehicleList.get(i).getModel());
            assertEquals(vehicleEntityList.get(i).getYear(), vehicleList.get(i).getYear());
        }
    }
}