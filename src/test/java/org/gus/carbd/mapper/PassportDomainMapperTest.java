package org.gus.carbd.mapper;

import org.gus.carbd.domain.Passport;
import org.gus.carbd.entity.PassportEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class PassportDomainMapperTest {

    @InjectMocks
    PassportDomainMapperImpl passportDomainMapper;

    @Test
    void toPassportAllDataTest() {
        var result = passportDomainMapper.toPassport(new PassportEntity(1, "3333", "4444", null));
        assertEquals("3333", result.getSeries());
        assertEquals("4444", result.getNumber());
        assertEquals(1, result.getPassport_id());
    }

    @Test
    void toPassportNullDataTest() {
        assertNull(passportDomainMapper.toPassport(null));
    }

    @Test
    void toPassportEntityAllDataTest() {
        var result = passportDomainMapper.toPassportEntity(new Passport(1, "3333", "4444"));
        assertEquals("3333", result.getSeries());
        assertEquals("4444", result.getNumber());
        assertEquals(1, result.getPassport_id());
        assertNull(result.getPerson());
    }

    @Test
    void toPassportEntityNullDataTest() {
        assertNull(passportDomainMapper.toPassportEntity(null));
    }

    @Test
    void updatePassportAllDataTest() {
        PassportEntity passportEntity = preparePassportEntity();

        passportDomainMapper.updatePassportEntity(passportEntity, new Passport(2, "3333", "4444"));
        assertEquals(2, passportEntity.getPassport_id());
        assertEquals("3333", passportEntity.getSeries());
        assertEquals("4444", passportEntity.getNumber());
        assertNull(passportEntity.getPerson());
    }

    @Test
    void updatePassportPartOfDataTest() {
        PassportEntity passportEntity = preparePassportEntity();

        passportDomainMapper.updatePassportEntity(passportEntity, new Passport(null, null, "4444"));
        assertEquals(1, passportEntity.getPassport_id());
        assertEquals("1111", passportEntity.getSeries());
        assertEquals("4444", passportEntity.getNumber());
        assertNull(passportEntity.getPerson());
    }

    @Test
    void updatePassportNullDataTest() {
        PassportEntity passportEntity = preparePassportEntity();

        passportDomainMapper.updatePassportEntity(passportEntity, null);
        assertEquals(1, passportEntity.getPassport_id());
        assertEquals("1111", passportEntity.getSeries());
        assertEquals("2222", passportEntity.getNumber());
        assertNull(passportEntity.getPerson());
    }

    private PassportEntity preparePassportEntity() {
        return new PassportEntity(1, "1111", "2222", null);
    }
}