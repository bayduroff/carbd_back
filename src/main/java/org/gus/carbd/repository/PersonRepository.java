package org.gus.carbd.repository;

import org.gus.carbd.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByPassport(String passport);

    boolean existsByPassport (String passport);
}
