package org.gus.carbd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.gus.carbd.entity.Person;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDto {
    private Integer vin;
    private String brand;
    private String model;
    private Integer year;
    private Set<Person> people;
}
