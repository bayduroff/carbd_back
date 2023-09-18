package org.gus.carbd.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @JsonBackReference
    private Set<Person> people;

    @Override
    public boolean equals(final Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof Vehicle)) return false;
        final Vehicle other = (Vehicle) o;
        if (!other.canEqual(this)) return false;
        final Object this$vin = this.getVin();
        final Object other$vin = other.getVin();
        if (this$vin == null ? other$vin != null : !this$vin.equals(other$vin)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof Vehicle;
    }

    @Override
    public int hashCode() {
        return 59;
    }
}
