package org.gus.carbd.repository;

import org.gus.carbd.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {
    Optional<PersonEntity> findPersonByPassportSeriesAndPassportNumber(String series, String number);

    boolean existsByPassportSeriesAndPassportNumber(String series, String number);
}
