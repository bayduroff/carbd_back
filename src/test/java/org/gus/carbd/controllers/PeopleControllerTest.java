package org.gus.carbd.controllers;

import org.gus.carbd.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PeopleControllerTest {

    @InjectMocks
    private PeopleController peopleController;

    @Mock
    private PersonRepository personRepositoryMock;

    @Test
    void getPeopleList() {
    }

    @Test
    void getPersonById() {
    }

    @Test
    void addPerson() {
    }

    @Test
    void deletePersonById() {
    }

    @Test
    void editPersonById() {
    }

    @Test
    void getPersonVehiclesByPersonId() {
    }

    @Test
    void updatePersonAssignVehicle() {
    }

    @Test
    void updatePersonUnAssignVehicle() {
    }

    @Test
    void getPersonByPassport() {
    }

    @Test
    void getPersonVehiclesByPassport() {
    }
}