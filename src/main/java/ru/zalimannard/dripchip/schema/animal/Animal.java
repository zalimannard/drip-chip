package ru.zalimannard.dripchip.schema.animal;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.ownedtype.type.AnimalType;
import ru.zalimannard.dripchip.schema.animal.visitedlocation.VisitedLocation;
import ru.zalimannard.dripchip.schema.location.Location;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Column(name = "animal_types")
    private Set<AnimalType> animalTypes = new HashSet<>();

    @Column(name = "weight")
    private Float weight;

    @Column(name = "length")
    private Float length;

    @Column(name = "height")
    private Float height;

    @Column(name = "gender")
    private AnimalGender gender;

    @Column(name = "life_status")
    private AnimalLifeStatus lifeStatus;

    @Column(name = "chipping_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipper_id")
    private Account chipper;

    @ManyToOne
    @JoinColumn(name = "chippingLocation_id")
    private Location chippingLocation;

    @Column(name = "death_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deathDateTime;

    @OneToMany(mappedBy = "animal")
    private List<VisitedLocation> visitedLocations;

}
