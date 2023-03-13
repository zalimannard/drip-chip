package ru.zalimannard.dripchip.schema.animal.visitedlocation;

import jakarta.persistence.*;
import lombok.Data;
import ru.zalimannard.dripchip.schema.animal.Animal;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.Date;

@Entity
@Table(name = "visitedLocations")
@Data
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

}
