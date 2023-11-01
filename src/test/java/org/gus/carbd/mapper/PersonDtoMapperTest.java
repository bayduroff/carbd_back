package org.gus.carbd.mapper;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.dto.PersonDto;
import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.gus.carbd.entity.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonDtoMapperTest {

    @InjectMocks
    private PersonDtoMapperImpl personDtoMapper;

    @Mock
    private PassportDtoMapperImpl passportDtoMapperMock;

    @Test
    void toPersonDtoAllDataTest() {
        PassportDto passportDto = new PassportDto("1111", "2222");
        Set<VehicleEntity> vehicleSet = prepareVehicleSet();
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), vehicleSet);

        doReturn(passportDto).when(passportDtoMapperMock).toPassportDto(any(PassportEntity.class));

        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals(passportDto, result.getPassportDto());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertEquals(vehicleSet, result.getVehicles());
    }

    @Test
    void toPersonDtoPartOfDataTest() {
        PersonEntity person = new PersonEntity(1, "Test", "Testov", null,
                null, null);

        doReturn(null).when(passportDtoMapperMock).toPassportDto(any());

        var result = personDtoMapper.toPersonDto(person);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertNull(result.getPassportDto());
        assertNull(result.getPatronymic());
        assertNull(result.getVehicles());
    }

    @Test
    void toPersonDtoNullDataTest() {
        assertNull(personDtoMapper.toPersonDto(null));
    }

    @Test
    void toPersonAllDataTest() {
        PassportEntity passport = preparePassport();
        Set<VehicleEntity> vehicleSet = prepareVehicleSet();
        PersonDto personDto = new PersonDto(1, new PassportDto(), "Test", "Testov",
                "Testovich", vehicleSet);

        doReturn(passport).when(passportDtoMapperMock).toPassport(any(PassportDto.class));

        var result = personDtoMapper.toPerson(personDto);
        assertEquals(1, result.getId());
        assertEquals("Test", result.getName());
        assertEquals("Testov", result.getSurname());
        assertEquals("Testovich", result.getPatronymic());
        assertEquals(vehicleSet, result.getVehicles());
        assertEquals(passport, result.getPassport());
        assertNotNull(result.getPassport().getPerson());
    }

    @Test
    void toPersonNullDataTest() {
        assertNull(personDtoMapper.toPerson(null));
    }

    @Test
    void toPersonDtoListAllDataTest() {
        PassportDto passportDto1 = new PassportDto("1111", "2222");
        PassportDto passportDto2 = new PassportDto("3333", "4444");
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        PersonEntity person2 = new PersonEntity(2, "Test2", "Testov2",
                "Testovich2", new PassportEntity(), null);
        List<PersonEntity> personList = List.of(person, person2);

        when(passportDtoMapperMock.toPassportDto(any(PassportEntity.class)))
                .thenReturn(passportDto1)
                .thenReturn(passportDto2);

        var result = personDtoMapper.toPersonDtoList(personList);
        assertEquals(personList.size(), result.size());
        assertLists(personList, result);
        assertEquals(passportDto1, result.get(0).getPassportDto());
        assertEquals(passportDto2, result.get(1).getPassportDto());
    }

    @Test
    void toPersonDtoListNullDataTest() {
        assertNull(personDtoMapper.toPersonDtoList(null));
    }

    @Test
    void toPersonDtoSetAllDataTest() {
        var personSet = preparePersonSet();
        PassportDto passportDto1 = new PassportDto("1111", "2222");
        PassportDto passportDto2 = new PassportDto("3333", "4444");

        when(passportDtoMapperMock.toPassportDto(any(PassportEntity.class)))
                .thenReturn(passportDto1)
                .thenReturn(passportDto2);

        var result = personDtoMapper.toPersonDtoSet(personSet);
        assertEquals(personSet.size(), result.size());
        var resultList = result.stream().sorted(Comparator.comparing(PersonDto::getId)).toList();
        var personList = personSet.stream().sorted(Comparator.comparing(PersonEntity::getId)).toList();
        assertLists(personList, resultList);
        assertEquals(passportDto1, resultList.get(0).getPassportDto());
        assertEquals(passportDto2, resultList.get(1).getPassportDto());
    }

    @Test
    void toPersonDtoSetNullDataTest() {
        assertNull(personDtoMapper.toPersonDtoSet(null));
    }

    @Test
    void updatePersonFullChangeTest() {
        PassportEntity passport = preparePassport();
        PassportDto passportDto = new PassportDto("3333", "4444");
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", passport, null);
        PersonDto changedDto = new PersonDto(2, passportDto, "ChangeTest", "ChangeTestov",
                "ChangeTestovich", Collections.emptySet());

        doCallRealMethod().when(passportDtoMapperMock).updatePassport(any(PassportEntity.class), any(PassportDto.class));

        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(2, person.getId());
        assertEquals("3333", person.getPassport().getSeries());
        assertEquals("4444", person.getPassport().getNumber());
        assertNotNull(person.getPassport().getPerson());
        assertEquals("ChangeTest", person.getName());
        assertEquals("ChangeTestov", person.getSurname());
        assertEquals("ChangeTestovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    @Test
    void updatePersonPartOfDataChangeTest() {
        PassportEntity passport = preparePassport();
        PassportDto passportDto = new PassportDto("3333", "4444");
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", passport, Collections.emptySet());
        PersonDto changedDto = new PersonDto(null, passportDto, null, "ChangeTestov",
                null, null);

        doCallRealMethod().when(passportDtoMapperMock).updatePassport(any(PassportEntity.class), any(PassportDto.class));

        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(1, person.getId());
        assertEquals("3333", person.getPassport().getSeries());
        assertEquals("4444", person.getPassport().getNumber());
        assertNotNull(person.getPassport().getPerson());
        assertEquals("Test", person.getName());
        assertEquals("ChangeTestov", person.getSurname());
        assertEquals("Testovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    @Test
    void updatePersonNoChangeTest() {
        PassportEntity passport = preparePassport();
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", passport, Collections.emptySet());
        PersonDto changedDto = new PersonDto();

        personDtoMapper.updatePerson(person, changedDto);
        assertEquals(1, person.getId());
        assertEquals("1111", person.getPassport().getSeries());
        assertEquals("2222", person.getPassport().getNumber());
        assertNotNull(person.getPassport().getPerson());
        assertEquals("Test", person.getName());
        assertEquals("Testov", person.getSurname());
        assertEquals("Testovich", person.getPatronymic());
        assertEquals(Collections.emptySet(), person.getVehicles());
    }

    private void assertLists(List<PersonEntity> personList, List<PersonDto> personDtoList) {
        for (int i = 0; i < personDtoList.size(); i++) {
            assertEquals(personList.get(i).getId(), personDtoList.get(i).getId());
            assertEquals(personList.get(i).getName(), personDtoList.get(i).getName());
            assertEquals(personList.get(i).getSurname(), personDtoList.get(i).getSurname());
            assertEquals(personList.get(i).getPatronymic(), personDtoList.get(i).getPatronymic());
            assertEquals(personList.get(i).getVehicles(), personDtoList.get(i).getVehicles());
        }
    }

    private PassportEntity preparePassport() {
        return new PassportEntity(1, "1111", "2222", null);
    }

    private Set<VehicleEntity> prepareVehicleSet() {
        VehicleEntity vehicle1 = new VehicleEntity(1, "test", "test1", 123, Collections.emptySet());
        VehicleEntity vehicle2 = new VehicleEntity(2, "test2", "test2", 321, Collections.emptySet());
        Set<VehicleEntity> vehicleSet = new HashSet<>();
        vehicleSet.add(vehicle1);
        vehicleSet.add(vehicle2);
        return vehicleSet;
    }

    private Set<PersonEntity> preparePersonSet() {
        PersonEntity person = new PersonEntity(1, "Test", "Testov",
                "Testovich", new PassportEntity(), null);
        PersonEntity person2 = new PersonEntity(2, "Test2", "Testov2",
                "Testovich2", new PassportEntity(), null);
        Set<PersonEntity> personSet = new HashSet<>();
        personSet.add(person);
        personSet.add(person2);
        return personSet;
    }
}