package org.gus.carbd.mapper;

import org.gus.carbd.dto.PassportDto;
import org.gus.carbd.entity.PassportEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
        var result = passportDtoMapper.toPassport(new PassportDto("3333", "4444"));
        assertEquals("3333", result.getSeries());
        assertEquals("4444", result.getNumber());
        assertNull(result.getPerson());
        assertNull(result.getPassport_id());
    }

    @Test
    void toPassportNullDataTest() {
        assertNull(passportDtoMapper.toPassport(null));
    }

    @Test
    void updatePassportAllDataTest() {
        PassportEntity passport = preparePassport();

        passportDtoMapper.updatePassport(passport, new PassportDto("3333", "4444"));
        assertEquals(1, passport.getPassport_id());
        assertEquals("3333", passport.getSeries());
        assertEquals("4444", passport.getNumber());
        assertNull(passport.getPerson());
    }

    @Test
    void updatePassportPartOfDataTest() {
        PassportEntity passport = preparePassport();

        passportDtoMapper.updatePassport(passport, new PassportDto(null, "4444"));
        assertEquals(1, passport.getPassport_id());
        assertEquals("1111", passport.getSeries());
        assertEquals("4444", passport.getNumber());
        assertNull(passport.getPerson());
    }

    @Test
    void updatePassportNullDataTest() {
        PassportEntity passport = preparePassport();

        passportDtoMapper.updatePassport(passport, null);
        assertEquals(1, passport.getPassport_id());
        assertEquals("1111", passport.getSeries());
        assertEquals("2222", passport.getNumber());
        assertNull(passport.getPerson());
    }

    private PassportEntity preparePassport() {
        return new PassportEntity(1, "1111", "2222", null);
    }
}