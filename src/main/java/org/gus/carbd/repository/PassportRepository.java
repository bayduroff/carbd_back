package org.gus.carbd.repository;

import org.gus.carbd.entity.Passport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassportRepository extends JpaRepository<Passport, Integer> {

}