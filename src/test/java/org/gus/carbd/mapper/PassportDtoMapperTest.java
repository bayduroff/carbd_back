package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.dto.PassportDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PassportDtoMapperTest {

    @InjectMocks
    private PassportDtoMapperImpl passportDtoMapper;

    @Test
    void toPassportDtoAllDataTest() {
        var result = passportDtoMapper.toPassportDto(preparePassport());
        assertEquals("1111", result.getSeries());
        assertEquals("2222", result.getNumber());
    }

    @Test
    void toPassportDtoNullDataTest() {
        assertNull(passportDtoMapper.toPassportDto(null));
    }

    @Test
    void toPassportAllDataTest() {
        var result = passportDtoMapper.toPassport(new PassportDto(1, "3333", "4444"));
        assertEquals("3333", result.getSeries());
        assertEquals("4444", result.getNumber());
        assertEquals(1, result.getPassport_id());
    }

    @Test
    void toPassportNullDataTest() {
        assertNull(passportDtoMapper.toPassport(null));
    }

    @Test
    void toPassportDtoListAllDataTest() {
        Passport passport1 = new Passport(1, "1111", "2222");
        Passport passport2 = new Passport(2, "3333", "4444");
        List<Passport> passportList = List.of(passport1, passport2);

        var result = passportDtoMapper.toPassportDtoList(passportList);
        assertEquals(passportList.size(), result.size());
        assertLists(passportList, result);
    }

    @Test
    void toPassportDtoListEmptyListTest() {
        assertTrue(passportDtoMapper.toPassportDtoList(Collections.emptyList()).isEmpty());
    }

    @Test
    void toPassportDtoListNullDataTest() {
        assertNull(passportDtoMapper.toPassportDtoList(null));
    }

    private void assertLists(List<Passport> passportList, List<PassportDto> passportDtoList) {
        for (int i = 0; i < passportDtoList.size(); i++) {
            assertEquals(passportList.get(i).getPassport_id(), passportDtoList.get(i).getPassport_id());
            assertEquals(passportList.get(i).getSeries(), passportDtoList.get(i).getSeries());
            assertEquals(passportList.get(i).getNumber(), passportDtoList.get(i).getNumber());
        }
    }

    private Passport preparePassport() {
        return new Passport(1, "1111", "2222");
    }
}