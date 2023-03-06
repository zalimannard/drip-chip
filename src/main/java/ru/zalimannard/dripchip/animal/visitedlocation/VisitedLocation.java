package ru.zalimannard.dripchip.animal.visitedlocation;

import jakarta.persistence.*;
import lombok.Data;
import ru.zalimannard.dripchip.animal.Animal;
import ru.zalimannard.dripchip.location.Location;

import java.sql.Timestamp;

@Entity
@Table(name = "visitedLocations")
@Data
public class VisitedLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dateTimeOfVisitLocationPoint")
    private Timestamp dateTimeOfVisitLocationPoint;

    @ManyToOne
    @JoinColumn(name = "animal")
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "location")
    private Location location;

}
