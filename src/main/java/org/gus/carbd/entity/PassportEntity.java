package org.gus.carbd.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "passport")
public class PassportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Integer passport_id;

    @Column(name = "series")
    private String series;

    @Column(name = "number")
    private String number;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @JsonBackReference
    private PersonEntity person;

    @Override
    public boolean equals(final Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof PassportEntity)) return false;
        final PassportEntity other = (PassportEntity) o;
        if (!other.canEqual(this)) return false;
        final Object this$passport_id = this.getPassport_id();
        final Object other$passport_id = other.getPassport_id();
        if (this$passport_id == null ? other$passport_id != null : !this$passport_id.equals(other$passport_id))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PassportEntity;
    }

    @Override
    public int hashCode() {
        return 59;
    }
}
