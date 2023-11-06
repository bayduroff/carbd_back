package org.gus.carbd.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Integer vin;
    private String brand;
    private String model;
    private Integer year;
}
