package org.gus.carbd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {
    private Integer id;
    private PassportDto passportDto;
    private String name;
    private String surname;
    private String patronymic;
}
