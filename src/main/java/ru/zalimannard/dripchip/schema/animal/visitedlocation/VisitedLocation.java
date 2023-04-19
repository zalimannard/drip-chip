package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "visitedLocations")
@Getter
@Setter
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class VisitedLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dateTimeOfVisitLocationPoint")
    private Date dateTimeOfVisitLocationPoint;

    @ManyToOne
    @JoinColumn(name = "animal")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        VisitedLocation that = (VisitedLocation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
