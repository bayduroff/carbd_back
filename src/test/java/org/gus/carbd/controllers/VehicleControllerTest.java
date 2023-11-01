package org.gus.carbd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.entity.VehicleEntity;
import org.gus.carbd.mapper.PersonDtoMapperImpl;
import org.gus.carbd.mapper.VehicleDtoMapperImpl;
import org.gus.carbd.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VehicleController.class)
@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    private static final String BASE_URL = "/vehicles";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private VehicleService vehicleService;

    @MockBean
    private PersonDtoMapperImpl personDtoMapper;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private VehicleDtoMapperImpl vehicleDtoMapper;

    @Test
    void getVehiclesListTest() throws Exception {
        VehicleEntity vehicle = new VehicleEntity(1, "BMW", "X5",
                1234, null);
        VehicleEntity vehicle2 = new VehicleEntity(2, "Lada", "Kalina",
                4321, null);
        List<VehicleEntity> vehicleList = List.of(vehicle, vehicle2);

        doReturn(vehicleList).when(vehicleService).getVehiclesList();

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].brand").value("BMW"))
                .andExpect(jsonPath("$.[1].brand").value("Lada"));
    }

    @Test
    void getVehicleByVinTest() throws Exception {
        doReturn(prepareVehicle()).when(vehicleService).getVehicleByVin(anyInt());

        mockMvc.perform(get((BASE_URL.concat("/1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("BMW"));
    }

    @Test
    void addVehicleTest() throws Exception {
        doNothing().when(vehicleService).addVehicle(any(VehicleDto.class));

        mockMvc.perform(post((BASE_URL.concat("/add")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prepareVehicleDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteVehicleByVinTest() throws Exception {
        doNothing().when(vehicleService).deleteVehicleByVin(anyInt());

        mockMvc.perform(delete((BASE_URL.concat("/1"))))
                .andExpect(status().isOk());
    }

    @Test
    void editVehicleByVinTest() throws Exception {
        doNothing().when(vehicleService).editVehicleByVin(anyInt(), any(VehicleDto.class));

        mockMvc.perform(patch((BASE_URL.concat("/1/edit")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prepareVehicleDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getVehicleOwnersTest() throws Exception {
        PersonDto personDto1 = new PersonDto(1, new PassportDto(), "Test", "Testov",
                "Testovich", null);
        PersonDto personDto2 = new PersonDto(2, new PassportDto(), "Test2", "Testov2",
                "Testovich2", null);
        Set<PersonDto> personDtoSet = new HashSet<>();
        personDtoSet.add(personDto1);
        personDtoSet.add(personDto2);

        doReturn(personDtoSet).when(personDtoMapper).toPersonDtoSet(any());

        mockMvc.perform(get((BASE_URL.concat("/1/people"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("Test", "Test2")));
    }

    @Test
    void getVehicleOwnersPassportsTest() throws Exception {
        List<String> passportList = List.of("12345", "67890");

        doReturn(passportList).when(vehicleService).getVehicleOwnersPassports(anyInt());

        mockMvc.perform(get((BASE_URL.concat("/1/peoplepass"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", containsInAnyOrder("12345", "67890")));
    }

    private VehicleEntity prepareVehicle() {
        return new VehicleEntity(1, "BMW", "X5",
                1234, null);
    }

    private VehicleDto prepareVehicleDto() {
        return new VehicleDto(1, "BMW", "X5",
                1234, null);
    }

}