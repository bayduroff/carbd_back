package org.gus.carbd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Person {
    private Integer id;

    private String name;

    private String surname;

    private String patronymic;

    private Passport passport;
}
