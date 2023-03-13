package ru.zalimannard.dripchip.schema.animal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.*;

@Entity
@Table(name = "animals")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToMany
    @Column(name = "animalTypes")
    private Set<AnimalType> animalTypes = new HashSet<>();

    @Column(name = "weight")
    private Float weight;

    @Column(name = "length")
    private Float length;

    @Column(name = "height")
    private Float height;

    @Column(name = "gender")
    private AnimalGender gender;

    @Column(name = "lifeStatus")
    private AnimalLifeStatus lifeStatus;

    @Column(name = "chippingDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipperId")
    private Account chipper;

    @ManyToOne
    @JoinColumn(name = "chippingLocationId")
    private Location chippingLocation;

    @Column(name = "deathDateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDateTime;

    @OneToMany(mappedBy = "animal")
    private List<VisitedLocation> visitedLocations;

}