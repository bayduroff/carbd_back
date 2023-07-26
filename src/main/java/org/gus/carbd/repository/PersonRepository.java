package org.gus.carbd.repository;

import org.gus.carbd.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
