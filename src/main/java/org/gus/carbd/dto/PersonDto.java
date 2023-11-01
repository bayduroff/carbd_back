package org.gus.carbd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gus.carbd.entity.VehicleEntity;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Integer id;
    private PassportDto passportDto;
    private String name;
    private String surname;
    private String patronymic;
    private Set<VehicleEntity> vehicles;
}
