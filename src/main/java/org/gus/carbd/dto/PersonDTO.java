package org.gus.carbd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.gus.carbd.entity.Vehicle;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonDTO {
    private Integer id;
    private String passport;
    private String name;
    private String surname;
    private String patronymic;
    private Set<Vehicle> vehicles;
}
