package org.gus.carbd.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.util.Set;

@Entity
@Table(name = "person")
//добавить equals/hashcode
//Влад Михалчек, прочитать про equals hashcode
//equals/hashcode lambock неполный
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "passport")
    private String passport;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    //либо подтягивание строк, либо many_to_many
    @ManyToMany
    @JoinTable(
            name = "person_vehicle",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_vin"))
    @JsonBackReference
    private Set<Vehicle> vehicles;

    public Person() {
    }

    public Person(Integer id, String passport, String name, String surname, String patronymic) {
        this.id = id;
        this.passport = passport;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public Person(Integer id, String passport, String name, String surname, String patronymic, Set<Vehicle> vehicles) {
        this.id = id;
        this.passport = passport;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.vehicles = vehicles;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", passport=" + passport +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
