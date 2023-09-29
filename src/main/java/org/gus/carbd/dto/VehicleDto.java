package org.gus.carbd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gus.carbd.entity.Person;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Integer vin;
    private String brand;
    private String model;
    private Integer year;
    private Set<Person> people;
}
