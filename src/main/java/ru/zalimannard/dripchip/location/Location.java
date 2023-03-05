package ru.zalimannard.dripchip.location;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "latitude"})
        })
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

}
