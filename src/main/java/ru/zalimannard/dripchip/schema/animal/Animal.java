package ru.zalimannard.dripchip.schema.animal;

import jakarta.persistence.*;
import lombok.Data;
import ru.zalimannard.dripchip.schema.account.Account;
import ru.zalimannard.dripchip.schema.animal.gender.AnimalGender;
import ru.zalimannard.dripchip.schema.animal.lifestatus.AnimalLifeStatus;
import ru.zalimannard.dripchip.schema.animal.type.AnimalType;
import ru.zalimannard.dripchip.schema.location.Location;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "animals")
@Data
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    private Timestamp chippingDateTime;

    @ManyToOne
    @JoinColumn(name = "chipperId")
    private Account chipper;

    @ManyToOne
    @JoinColumn(name = "chippingLocationId")
    private Location chippingLocation;

    @Column(name = "deathDateTime")
    private Timestamp deathDateTime;

}
