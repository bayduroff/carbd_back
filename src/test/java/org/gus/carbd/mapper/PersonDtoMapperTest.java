package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.domain.Person;
import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDtoMapperTest {

    @InjectMocks
    private PersonDtoMapperImpl personDtoMapper;

    @Mock
    private PassportDtoMapperImpl passportDtoMapperMock;

    @Test
    void toPersonDtoAllDataTest() {
        PassportDto passportDto = new PassportDto(1, "1111", "2222");
        Person person = new Person(1, "Test", "Testov", "Testovich", new Passport());

        doReturn(passportDto).when(passportDtoMapperMock).toPassportDto(any(Passport.class));

        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals(passportDto, result.getPassportDto());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
    }

    @Test
    void toPersonDtoPartOfDataTest() {
        Person person = new Person(1, "Test", "Testov", null, null);

        doReturn(null).when(passportDtoMapperMock).toPassportDto(any());

        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertNull(result.getPassportDto());
        assertNull(result.getPatronymic());
    }

    @Test
    void toPersonDtoNullDataTest() {
        assertNull(personDtoMapper.toPersonDto(null));
    }

    @Test
    void toPersonDtoListAllDataTest() {
        PassportDto passportDto1 = new PassportDto(1, "1111", "2222");
        PassportDto passportDto2 = new PassportDto(2, "3333", "4444");
        Person person1 = new Person(1, "Test", "Testov", "Testovich", new Passport());
        Person person2 = new Person(2, "Test2", "Testov2", "Testovich2", new Passport());
        List<Person> personList = List.of(person1, person2);

        when(passportDtoMapperMock.toPassportDto(any(Passport.class)))
                .thenReturn(passportDto1)
                .thenReturn(passportDto2);

        var result = personDtoMapper.toPersonDtoList(personList);
        assertEquals(personList.size(), result.size());
        assertLists(personList, result);
        assertEquals(passportDto1, result.get(0).getPassportDto());
        assertEquals(passportDto2, result.get(1).getPassportDto());
    }

    @Test
    void toPersonDtoListEmptyListTest() {
        var result = personDtoMapper.toPersonDtoList(Collections.emptyList());

        assertTrue(result.isEmpty());
        verify(passportDtoMapperMock, never()).toPassportDto(any(Passport.class));
    }

    @Test
    void toPersonDtoListNullDataTest() {
        assertNull(personDtoMapper.toPersonDtoList(null));
    }

    @Test
    void toPersonAllDataTest() {
        Passport passport = preparePassport();
        PersonDto personDto = new PersonDto(1, new PassportDto(),
                "Test", "Testov", "Testovich");

        doReturn(passport).when(passportDtoMapperMock).toPassport(any(PassportDto.class));

        var result = personDtoMapper.toPerson(personDto);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertEquals(passport, result.getPassport());
    }

    @Test
    void toPersonNullDataTest() {
        assertNull(personDtoMapper.toPerson(null));
    }

    private void assertLists(List<Person> personList, List<PersonDto> personDtoList) {
        for (int i = 0; i < personDtoList.size(); i++) {
            assertEquals(personList.get(i).getId(), personDtoList.get(i).getId());
            assertEquals(personList.get(i).getName(), personDtoList.get(i).getName());
            assertEquals(personList.get(i).getSurname(), personDtoList.get(i).getSurname());
            assertEquals(personList.get(i).getPatronymic(), personDtoList.get(i).getPatronymic());
        }
    }

    private Passport preparePassport() {
        return new Passport(1, "1111", "2222");
    }
}