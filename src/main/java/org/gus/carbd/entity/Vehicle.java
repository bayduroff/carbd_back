package org.gus.carbd.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "vehicle", schema = "public", catalog = "postgres")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vin")
    private Integer vin;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;
    @Column(name = "year")
    private Integer year;

    @ManyToMany(mappedBy = "vehicles")
    //@JsonManagedReference
    @JsonBackReference
    private Set<Person> people;

    public Vehicle (){}

    public Vehicle(Integer vin, String brand, String model, Integer year) {
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public Vehicle(Integer vin, String brand, String model, Integer year, Set<Person> people) {
        this.vin = vin;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.people = people;
    }


    @Override
    public String toString() {
        return "Vehicle{" +
                "vin=" + vin +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", year=" + year +
                '}';
    }

    public Integer getVin() {
        return vin;
    }

    public void setVin(Integer vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
}
