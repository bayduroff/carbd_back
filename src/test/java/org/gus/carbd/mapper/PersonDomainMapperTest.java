package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDomainMapperTest {

    @InjectMocks
    PersonDomainMapperImpl personDomainMapper;

    @Mock
    PassportDomainMapperImpl passportDomainMapperMock;

    @Test
    void toPersonAllDataTest() {
        Passport passport = new Passport(1, "1111", "2222");
        PersonEntity personEntity = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);

        doReturn(passport).when(passportDomainMapperMock).toPassport(any(PassportEntity.class));

        var result = personDomainMapper.toPerson(personEntity);
        assertEquals(1, result.getId());
        assertEquals(passport, result.getPassport());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
    }

    @Test
    void toPersonPartOfDataTest() {
        PersonEntity personEntity = new PersonEntity(1, "Test", "Testov", null,
                null, null);

        doReturn(null).when(passportDomainMapperMock).toPassport(any());

        var result = personDomainMapper.toPerson(personEntity);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertNull(result.getPassport());
        assertNull(result.getPatronymic());
    }

    @Test
    void toPersonNullDataTest() {
        assertNull(personDomainMapper.toPerson(null));
    }

    @Test
    void toPersonListAllDataTest() {
        Passport passport1 = new Passport(1, "1111", "2222");
        Passport passport2 = new Passport(2, "3333", "4444");
        PersonEntity personEntity1 = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        PersonEntity personEntity2 = new PersonEntity(2, "Test2", "Testov2",
                "Testovich2", new PassportEntity(), null);
        List<PersonEntity> personEntityList = List.of(personEntity1, personEntity2);

        when(passportDomainMapperMock.toPassport(any(PassportEntity.class)))
                .thenReturn(passport1)
                .thenReturn(passport2);

        var resultList = personDomainMapper.toPersonList(personEntityList);
        assertLists(personEntityList, resultList);
        assertEquals(passport1, resultList.get(0).getPassport());
        assertEquals(passport2, resultList.get(1).getPassport());
    }

    @Test
    void toPersonListEmptyPersonEntitySetTest() {
        var result = personDomainMapper.toPersonList(Collections.emptyList());
        assertTrue(result.isEmpty());
        verify(passportDomainMapperMock, never()).toPassport(any());
    }

    @Test
    void toPersonListNullDataTest() {
        assertNull(personDomainMapper.toPersonList(null));
    }

    @Test
    void toPersonEntityAllDataTest() {
        PassportEntity passportEntity = new PassportEntity(1, "1111", "2222", null);
        Person person = new Person(1, "Test", "Testov", "Testovich", new Passport());

        doReturn(passportEntity).when(passportDomainMapperMock).toPassportEntity(any(Passport.class));

        var result = personDomainMapper.toPersonEntity(person);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertPassports(passportEntity, result.getPassport());
        assertNotNull(result.getPassport().getPerson());
    }

    @Test
    void toPersonEntityPartOfDataTest() {
        Person person = new Person(1, "Test", "Testov", null, null);

        doReturn(null).when(passportDomainMapperMock).toPassportEntity(any());

        var result = personDomainMapper.toPersonEntity(person);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertNull(result.getPassport());
        assertNull(result.getPatronymic());
    }

    @Test
    void toPersonEntityNullDataTest() {
        assertNull(personDomainMapper.toPersonEntity(null));
    }

    @Test
    void updatePersonEntityFullChangeTest() {
        PersonEntity personEntity = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        Passport changedPassport = new Passport(1, "3333", "4444");
        Person changedPerson = new Person(2, "ChangeTest", "ChangeTestov",
                "ChangeTestovich", changedPassport);

        doCallRealMethod().when(passportDomainMapperMock)
                .updatePassportEntity(any(PassportEntity.class), any(Passport.class));

        personDomainMapper.updatePersonEntity(personEntity, changedPerson);
        assertEquals(2, personEntity.getId());
        assertEquals("ChangeTest", personEntity.getName());
        assertEquals("ChangeTestov", personEntity.getSurname());
        assertEquals("ChangeTestovich", personEntity.getPatronymic());
        assertNull(personEntity.getVehicles());
        assertPassports(changedPassport, personEntity.getPassport());
        assertNotNull(personEntity.getPassport().getPerson());
    }

    @Test
    void updatePersonEntityPartOfDataChangeTest() {
        PersonEntity personEntity = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        Passport changedPassport = new Passport(1, "3333", "4444");
        Person changedPerson = new Person(null, null, "ChangeTestov",
                null, changedPassport);

        doCallRealMethod().when(passportDomainMapperMock)
                .updatePassportEntity(any(PassportEntity.class), any(Passport.class));

        personDomainMapper.updatePersonEntity(personEntity, changedPerson);
        assertEquals(1, personEntity.getId());
        assertEquals("Test", personEntity.getName());
        assertEquals("ChangeTestov", personEntity.getSurname());
        assertEquals("Testovich", personEntity.getPatronymic());
        assertNull(personEntity.getVehicles());
        assertPassports(changedPassport, personEntity.getPassport());
        assertNotNull(personEntity.getPassport().getPerson());
    }

    @Test
    void updatePersonEntityNoChangeTest() {
        PersonEntity personEntity = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        Person changedPerson = new Person();

        personDomainMapper.updatePersonEntity(personEntity, changedPerson);
        assertEquals(1, personEntity.getId());
        assertEquals("Test", personEntity.getName());
        assertEquals("Testov", personEntity.getSurname());
        assertEquals("Testovich", personEntity.getPatronymic());
        assertNull(personEntity.getVehicles());
        assertNotNull(personEntity.getPassport().getPerson());
        verify(passportDomainMapperMock, never()).updatePassportEntity(any(), any());
    }

    private void assertPassports(PassportEntity passportEntity1, PassportEntity passportEntity2) {
        assertEquals(passportEntity1.getPassport_id(), passportEntity2.getPassport_id());
        assertEquals(passportEntity1.getSeries(), passportEntity2.getSeries());
        assertEquals(passportEntity1.getNumber(), passportEntity2.getNumber());
    }

    private void assertPassports(Passport passport, PassportEntity passportEntity1) {
        assertEquals(passport.getPassport_id(), passportEntity1.getPassport_id());
        assertEquals(passport.getSeries(), passportEntity1.getSeries());
        assertEquals(passport.getNumber(), passportEntity1.getNumber());
    }

    private void assertLists(List<PersonEntity> personList, List<Person> personDtoList) {
        for (int i = 0; i < personDtoList.size(); i++) {
            assertEquals(personList.get(i).getId(), personDtoList.get(i).getId());
            assertEquals(personList.get(i).getName(), personDtoList.get(i).getName());
            assertEquals(personList.get(i).getSurname(), personDtoList.get(i).getSurname());
            assertEquals(personList.get(i).getPatronymic(), personDtoList.get(i).getPatronymic());
        }
    }
}