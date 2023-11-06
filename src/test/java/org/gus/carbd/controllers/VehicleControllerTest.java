package org.gus.carbd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gus.carbd.domain.Vehicle;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.mapper.PassportDtoMapperImpl;
import org.gus.carbd.mapper.PersonDtoMapperImpl;
import org.gus.carbd.mapper.VehicleDtoMapperImpl;
import org.gus.carbd.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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
    private VehicleService vehicleServiceMock;
    @MockBean
    private VehicleDtoMapperImpl vehicleDtoMapperMock;
    @MockBean
    private PersonDtoMapperImpl personDtoMapperMock;
    @MockBean
    private PassportDtoMapperImpl passportDtoMapperMock;

    @Test
    void getVehiclesListTest() throws Exception {
        VehicleDto vehicleDto1 = new VehicleDto(1, "BMW", "X5", 1234);
        VehicleDto vehicleDto2 = new VehicleDto(2, "Lada", "Kalina", 4321);
        List<VehicleDto> vehicleDtoList = List.of(vehicleDto1, vehicleDto2);

        doReturn(vehicleDtoList).when(vehicleDtoMapperMock).toVehicleDtoList(any());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].brand").value("BMW"))
                .andExpect(jsonPath("$.[1].brand").value("Lada"));
    }

    @Test
    void getVehicleByVinTest() throws Exception {
        doReturn(prepareVehicleDto()).when(vehicleDtoMapperMock).toVehicleDto(any());

        mockMvc.perform(get((BASE_URL.concat("/1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("BMW"));
    }

    @Test
    void addVehicleTest() throws Exception {
        doNothing().when(vehicleServiceMock).addVehicle(any(Vehicle.class));

        mockMvc.perform(post((BASE_URL.concat("/add")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prepareVehicleDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deleteVehicleByVinTest() throws Exception {
        doNothing().when(vehicleServiceMock).deleteVehicleByVin(anyInt());

        mockMvc.perform(delete((BASE_URL.concat("/1"))))
                .andExpect(status().isOk());
    }

    @Test
    void editVehicleByVinTest() throws Exception {
        doNothing().when(vehicleServiceMock).editVehicleByVin(anyInt(), any(Vehicle.class));

        mockMvc.perform(patch((BASE_URL.concat("/1/edit")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prepareVehicleDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getVehicleOwnersTest() throws Exception {
        PersonDto personDto1 = new PersonDto(1, new PassportDto(), "Test",
                "Testov", "Testovich");
        PersonDto personDto2 = new PersonDto(2, new PassportDto(), "Test2",
                "Testov2", "Testovich2");
        List<PersonDto> personDtoList = List.of(personDto1, personDto2);

        doReturn(personDtoList).when(personDtoMapperMock).toPersonDtoList(any());

        mockMvc.perform(get((BASE_URL.concat("/1/people")))).andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name", containsInAnyOrder("Test", "Test2")));
    }

    @Test
    void getVehicleOwnersPassportsTest() throws Exception {
        PassportDto passportDto1 = new PassportDto(1, "1234", "567890");
        PassportDto passportDto2 = new PassportDto(2, "0987", "654321");
        List<PassportDto> passportDtoList = List.of(passportDto1, passportDto2);

        doReturn(passportDtoList).when(passportDtoMapperMock).toPassportDtoList(any());

        mockMvc.perform(get((BASE_URL.concat("/1/peoplepass"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].series", containsInAnyOrder("1234", "0987")))
                .andExpect(jsonPath("$.[*].number", containsInAnyOrder("567890", "654321")));
    }

    private VehicleDto prepareVehicleDto() {
        return new VehicleDto(1, "BMW", "X5", 1234);
    }
}