package org.gus.carbd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gus.carbd.domain.Person;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.dto.VehicleDto;
import org.gus.carbd.mapper.PersonDtoMapperImpl;
import org.gus.carbd.mapper.VehicleDtoMapperImpl;
import org.gus.carbd.service.PersonService;
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

@WebMvcTest(controllers = PersonController.class)
@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    private static final String BASE_URL = "/people";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personServiceMock;
    @MockBean
    private PersonDtoMapperImpl personDtoMapperMock;
    @MockBean
    private VehicleDtoMapperImpl vehicleDtoMapperMock;

    @Test
    void getPeopleListTest() throws Exception {
        PersonDto personDto1 = preparePersonDto();
        PersonDto personDto2 = new PersonDto(2, new PassportDto(), "Test2", "Testov2",
                "Testovich2");
        List<PersonDto> personDtoList = List.of(personDto1, personDto2);

        doReturn(personDtoList).when(personDtoMapperMock).toPersonDtoList(any());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Test"))
                .andExpect(jsonPath("$.[1].name").value("Test2"));
    }

    @Test
    void getPersonByIdTest() throws Exception {
        doReturn(preparePersonDto()).when(personDtoMapperMock).toPersonDto(any());

        mockMvc.perform(get(BASE_URL.concat("/1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void addPersonTest() throws Exception {
        doNothing().when(personServiceMock).addPerson(any(Person.class));

        mockMvc.perform(post(BASE_URL.concat("/add"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preparePersonDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deletePersonByIdTest() throws Exception {
        doNothing().when(personServiceMock).deletePersonById(anyInt());

        mockMvc.perform(delete(BASE_URL.concat("/1")))
                .andExpect(status().isOk());
    }

    @Test
    void editPersonByIdTest() throws Exception {
        doNothing().when(personServiceMock).editPersonById(anyInt(), any(Person.class));

        mockMvc.perform(patch(BASE_URL.concat("/1/edit"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preparePersonDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getPersonVehiclesByPersonIdTest() throws Exception {
        doReturn(prepareVehicleDtoList()).when(vehicleDtoMapperMock).toVehicleDtoList(any());

        mockMvc.perform(get(BASE_URL.concat("/1/vehicles")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].brand", containsInAnyOrder("test", "test2")));
    }

    @Test
    void updatePersonAssignVehicleTest() throws Exception {
        doNothing().when(personServiceMock).updatePersonAssignVehicle(anyInt(), anyInt());

        mockMvc.perform(post(BASE_URL.concat("/1/vehicles/1")))
                .andExpect(status().isOk());
    }

    @Test
    void updatePersonUnAssignVehicleTest() throws Exception {
        doNothing().when(personServiceMock).updatePersonUnAssignVehicle(anyInt(), anyInt());

        mockMvc.perform(patch(BASE_URL.concat("/1/vehicles/1")))
                .andExpect(status().isOk());
    }

    @Test
    void getPersonByPassportTest() throws Exception {
        doReturn(preparePersonDto()).when(personDtoMapperMock).toPersonDto(any());

        mockMvc.perform(get(BASE_URL.concat("/search?series=1234&number=567890")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void getPersonVehiclesByPassportTest() throws Exception {
        doReturn(prepareVehicleDtoList()).when(vehicleDtoMapperMock).toVehicleDtoList(any());

        mockMvc.perform(get(BASE_URL.concat("/search/vehicles?series=1234&number=567890")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].brand", containsInAnyOrder("test", "test2")));
    }

    private List<VehicleDto> prepareVehicleDtoList() {
        VehicleDto vehicleDto1 = new VehicleDto(1, "test", "test1", 123);
        VehicleDto vehicleDto2 = new VehicleDto(2, "test2", "test2", 321);
        return List.of(vehicleDto1, vehicleDto2);
    }

    private PersonDto preparePersonDto() {
        return new PersonDto(1, new PassportDto(), "Test", "Testov",
                "Testovich");
    }
}