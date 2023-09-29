package org.gus.carbd.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.Person;
import org.gus.carbd.entity.Vehicle;
import org.gus.carbd.mapper.PersonDtoMapperImpl;
import org.gus.carbd.mapper.VehicleDtoMapperImpl;
import org.gus.carbd.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PeopleController.class)
@ExtendWith(MockitoExtension.class)
public class PeopleControllerTest {

    private static final String BASE_URL = "/people";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonService personService;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private PersonDtoMapperImpl personDtoMapper;

    @MockBean(answer = Answers.CALLS_REAL_METHODS)
    private VehicleDtoMapperImpl vehicleDtoMapper;

    @Test
    void getPeopleListTest() throws Exception {
        Person person = new Person(1, "12345", "Test", "Testov",
                "Testovich", null);
        Person person2 = new Person(2, "54321", "Test2", "Testov2",
                "Testovich2", null);
        List<Person> personList = List.of(person, person2);

        doReturn(personList).when(personService).getPeopleList();

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Test"))
                .andExpect(jsonPath("$.[1].name").value("Test2"));
    }

    @Test
    void getPersonByIdTest() throws Exception {
        doReturn(preparePerson()).when(personService).getPersonById(anyInt());

        mockMvc.perform(get((BASE_URL.concat("/1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void addPersonTest() throws Exception {
        doNothing().when(personService).addPerson(any(PersonDto.class));

        mockMvc.perform(post((BASE_URL.concat("/add")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preparePersonDto())))
                .andExpect(status().isOk());
    }

    @Test
    void deletePersonByIdTest() throws Exception {
        doNothing().when(personService).deletePersonById(anyInt());

        mockMvc.perform(delete((BASE_URL.concat("/1"))))
                .andExpect(status().isOk());
    }

    @Test
    void editPersonByIdTest() throws Exception {
        doNothing().when(personService).editPersonById(anyInt(), any(PersonDto.class));

        mockMvc.perform(patch((BASE_URL.concat("/1/edit")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(preparePersonDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getPersonVehiclesByPersonIdTest() throws Exception {
        doReturn(prepareVehicleSet()).when(personService).getPersonVehiclesByPersonId(anyInt());

        mockMvc.perform(get((BASE_URL.concat("/1/vehicles"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].brand", containsInAnyOrder("test", "test2")));
    }

    @Test
    void updatePersonAssignVehicleTest() throws Exception {
        doNothing().when(personService).updatePersonAssignVehicle(anyInt(), anyInt());

        mockMvc.perform(post((BASE_URL.concat("/1/vehicles/1"))))
                .andExpect(status().isOk());
    }

    @Test
    void updatePersonUnAssignVehicleTest() throws Exception {
        doNothing().when(personService).updatePersonUnAssignVehicle(anyInt(), anyInt());

        mockMvc.perform(patch((BASE_URL.concat("/1/vehicles/1"))))
                .andExpect(status().isOk());
    }

    @Test
    void getPersonByPassportTest() throws Exception {
        doReturn(preparePerson()).when(personService).getPersonByPassport(anyString());

        mockMvc.perform(get((BASE_URL.concat("/search/12345"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test"));
    }

    @Test
    void getPersonVehiclesByPassportTest() throws Exception {
        doReturn(prepareVehicleSet()).when(personService).getPersonVehiclesByPassport(anyString());

        mockMvc.perform(get((BASE_URL.concat("/search/12345/vehicles"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].brand", containsInAnyOrder("test", "test2")));
    }

    private Set<Vehicle> prepareVehicleSet() {
        Vehicle vehicle1 = new Vehicle(1, "test", "test1", 123, Collections.emptySet());
        Vehicle vehicle2 = new Vehicle(2, "test2", "test2", 321, Collections.emptySet());
        Set<Vehicle> vehicleSet = new HashSet<>();
        vehicleSet.add(vehicle1);
        vehicleSet.add(vehicle2);
        return vehicleSet;
    }

    private Person preparePerson() {
        return new Person(1, "12345", "Test", "Testov",
                "Testovich", null);
    }

    private PersonDto preparePersonDto() {
        return new PersonDto(1, "12345", "Test", "Testov",
                "Testovich", null);
    }
}