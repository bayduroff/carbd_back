package org.gus.carbd.repository;

import org.gus.carbd.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VehicleRepositoryDataJpaTest {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Test
    void saveVehicleEntityTest() {
        VehicleEntity vehicleEntity = prepareVehicleEntity();

        vehicleRepository.save(vehicleEntity);
        assertThat(vehicleEntity.getVin()).isGreaterThan(0);
    }

    @Test
    void findVehicleEntityByIdPositiveTest() {
        VehicleEntity preparedVehicleEntity = prepareVehicleEntity();
        vehicleRepository.save(preparedVehicleEntity);

        VehicleEntity vehicleEntity = vehicleRepository.findById(preparedVehicleEntity.getVin()).get();
        assertEquals(preparedVehicleEntity.getVin(), vehicleEntity.getVin());
    }

    @Test
    void findVehicleEntityByIdNegativeTest() {
        assertThrows(NoSuchElementException.class, () -> vehicleRepository.findById(0).get());
    }

    @Test
    void findAllVehicleEntitiesTest() {
        List<VehicleEntity> vehicleEntityList = vehicleRepository.findAll();
        assertThat(vehicleEntityList.size()).isGreaterThan(0);
    }

    @Test
    void updateVehicleEntityByIdTest() {
        VehicleEntity preparedVehicleEntity = prepareVehicleEntity();
        vehicleRepository.save(preparedVehicleEntity);

        VehicleEntity vehicleEntity = vehicleRepository.findById(preparedVehicleEntity.getVin()).get();
        assertEquals("brand", vehicleEntity.getBrand());
        vehicleEntity.setBrand("Tesla");

        vehicleRepository.save(vehicleEntity);
        assertEquals(preparedVehicleEntity.getVin(), vehicleEntity.getVin());
        assertEquals("Tesla", vehicleEntity.getBrand());
    }

    @Test
    void deleteVehicleEntityByIdTest() {
        VehicleEntity preparedVehicleEntity = prepareVehicleEntity();
        vehicleRepository.save(preparedVehicleEntity);
        VehicleEntity vehicleEntity = vehicleRepository.findById(preparedVehicleEntity.getVin()).get();
        assertNotNull(vehicleEntity);

        vehicleRepository.deleteById(preparedVehicleEntity.getVin());
        assertThrows(NoSuchElementException.class, () -> vehicleRepository.findById(preparedVehicleEntity.getVin()).get());
    }

    @Test
    void generatedNewIdHigherThanPrevious() {
        VehicleEntity vehicleEntity1 = new VehicleEntity(null, "brand", "model", 1234, null);
        vehicleRepository.save(vehicleEntity1);
        Integer firstId = vehicleEntity1.getVin();

        VehicleEntity vehicleEntity2 = new VehicleEntity(null, "brand2", "model2", 4321, null);
        vehicleRepository.save(vehicleEntity2);
        Integer secondId = vehicleEntity2.getVin();

        assertThat(secondId).isGreaterThan(firstId);
    }

    private VehicleEntity prepareVehicleEntity() {
        return new VehicleEntity(null, "brand", "model", 1234, null);
    }
}