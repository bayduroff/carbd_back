package org.gus.carbd.repository;

import org.gus.carbd.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByPassportSeriesAndPassportNumber(String series, String number);

    boolean existsByPassportSeriesAndPassportNumber(String series, String number);
}
