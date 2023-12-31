package org.gus.carbd.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "vehicle", schema = "public", catalog = "postgres")
public class VehicleEntity {

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

    @ToString.Exclude
    @ManyToMany(mappedBy = "vehicles", fetch = FetchType.LAZY)
    private Set<PersonEntity> people;

    @Override
    public boolean equals(final Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof VehicleEntity)) return false;
        final VehicleEntity other = (VehicleEntity) o;
        if (!other.canEqual(this)) return false;
        final Object this$vin = this.getVin();
        final Object other$vin = other.getVin();
        return Objects.equals(this$vin, other$vin);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof VehicleEntity;
    }

    @Override
    public int hashCode() {
        return 59;
    }
}
