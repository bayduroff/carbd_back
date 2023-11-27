package org.gus.carbd.repository;

import org.gus.carbd.entity.PassportEntity;
import org.gus.carbd.entity.PersonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryDataJpaTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    void savePersonEntityTest() {
        PersonEntity personEntity = preparePersonEntity();

        personRepository.save(personEntity);
        assertThat(personEntity.getId()).isGreaterThan(0);
    }

    @Test
    void findPersonEntityByIdPositiveTest() {
        PersonEntity preparedPersonEntity = preparePersonEntity();
        personRepository.save(preparedPersonEntity);

        PersonEntity personEntity = personRepository.findById(preparedPersonEntity.getId()).get();
        assertEquals(preparedPersonEntity.getId(), personEntity.getId());
    }

    @Test
    void findPersonEntityByIdNegativeTest() {
        assertThrows(NoSuchElementException.class, () -> personRepository.findById(0).get());
    }

    @Test
    void findAllPersonEntitiesTest() {
        List<PersonEntity> personEntityList = personRepository.findAll();
        assertThat(personEntityList.size()).isGreaterThan(0);
    }

    @Test
    void updatePersonEntityByIdTest() {
        PersonEntity preparedPersonEntity = preparePersonEntity();
        personRepository.save(preparedPersonEntity);

        PersonEntity personEntity = personRepository.findById(preparedPersonEntity.getId()).get();
        assertEquals("name", personEntity.getName());
        personEntity.setName("Vasya");

        personRepository.save(personEntity);
        assertEquals(preparedPersonEntity.getId(), personEntity.getId());
        assertEquals("Vasya", personEntity.getName());
    }

    @Test
    void deletePersonEntityByIdTest() {
        PersonEntity preparedPersonEntity = preparePersonEntity();
        personRepository.save(preparedPersonEntity);
        PersonEntity personEntity = personRepository.findById(preparedPersonEntity.getId()).get();
        assertNotNull(personEntity);

        personRepository.deleteById(preparedPersonEntity.getId());
        assertThrows(NoSuchElementException.class, () -> personRepository.findById(preparedPersonEntity.getId()).get());
    }

    @Test
    void findPersonByPassportSeriesAndPassportNumberPositiveTest() {
        PassportEntity passportEntity = new PassportEntity(null, "1122", "334455", null);
        PersonEntity personEntity = new PersonEntity(null, "name", "surname",
                "patronymic", passportEntity, null);
        passportEntity.setPerson(personEntity);
        personRepository.save(personEntity);
        Integer savedEntityId = personEntity.getId();

        var foundEntity =
                personRepository.findPersonByPassportSeriesAndPassportNumber("1122", "334455");
        assertEquals("1122", foundEntity.get().getPassport().getSeries());
        assertEquals("334455", foundEntity.get().getPassport().getNumber());
        assertEquals(savedEntityId, foundEntity.get().getId());
    }

    @Test
    void findPersonByPassportSeriesAndPassportNumberNegativeTest() {
        assertThrows(NoSuchElementException.class, () -> personRepository
                .findPersonByPassportSeriesAndPassportNumber("notExist", "notExist").get());
    }

    @Test
    void existsByPassportSeriesAndPassportNumberTest() {
        assertFalse(personRepository.existsByPassportSeriesAndPassportNumber("1122", "334455"));

        PassportEntity passportEntity = new PassportEntity(null, "1122", "334455", null);
        PersonEntity personEntity = new PersonEntity(null, "name", "surname",
                "patronymic", passportEntity, null);
        passportEntity.setPerson(personEntity);
        personRepository.save(personEntity);

        assertTrue(personRepository.existsByPassportSeriesAndPassportNumber("1122", "334455"));
    }

    @Test
    void generatedNewIdHigherThanPrevious() {
        PersonEntity personEntity1 = new PersonEntity(null, "name1", "surname1",
                "patronymic1", null, null);
        personRepository.save(personEntity1);
        Integer firstId = personEntity1.getId();

        PersonEntity personEntity2 = new PersonEntity(null, "name2", "surname2",
                "patronymic2", null, null);
        personRepository.save(personEntity2);
        Integer secondId = personEntity2.getId();

        assertThat(secondId).isGreaterThan(firstId);
    }

    private PersonEntity preparePersonEntity() {
        return new PersonEntity(null, "name", "surname", "patronymic", null, null);
    }
}